package model;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import javax.naming.directory.InvalidAttributesException;
import java.util.Iterator;

public class Brain {

    String fuzzyDriverPath;
    int amountOfVariables;

    public Brain(String fileName, int amountOfVariables){
        this.fuzzyDriverPath = fileName;
        this.amountOfVariables = amountOfVariables;
    }

    public String calculateResult(String[] variableNames, Double[] variableValues, String resultVariableName) throws Exception {
        if(variableNames.length != amountOfVariables || variableValues.length != amountOfVariables){
            throw new InvalidAttributesException("The amount of variables in method attributes is different, than what was specified during Brain() creation.");
        }

        FIS fis = FIS.load(this.fuzzyDriverPath,false);
        FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();

        for(int i=0; i<amountOfVariables; i++){
            fuzzyRuleSet.setVariable(variableNames[i], variableValues[i]);
        }

        fuzzyRuleSet.evaluate();

        Variable variable = fuzzyRuleSet.getVariable(resultVariableName);
        LinguisticTerm strongestLinguisticTerm = null;
        double strongestValue = 0;
        LinguisticTerm linguisticTerm;
        String str = "";
        for(Iterator it = variable.iteratorLinguisticTermNames(); it.hasNext(); ) {
            String key = (String)it.next();
            linguisticTerm = (LinguisticTerm)variable.getLinguisticTerms().get(key);

            double currentValue = linguisticTerm.getMembershipFunction().membership(variable.getValue());
            if(currentValue >= strongestValue){
                strongestLinguisticTerm = linguisticTerm;
                strongestValue = currentValue;
            }
        }

        assert strongestLinguisticTerm != null;
        return strongestLinguisticTerm.getTermName();
    }
}
