package model;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import javax.naming.directory.InvalidAttributesException;
import java.util.Iterator;

public class Brain {

    boolean didShowGraphs = false;

    String fuzzyDriverPath;
    int amountOfVariables;

    public Brain(String fileName, int amountOfVariables){
        this.fuzzyDriverPath = fileName;
        this.amountOfVariables = amountOfVariables;
    }

    public double calculateResultDouble(String[] variableNames, Double[] variableValues, String resultVariableName) throws Exception {
        if (variableNames.length != amountOfVariables || variableValues.length != amountOfVariables) {
            throw new InvalidAttributesException("The amount of variables in method attributes is different, than what was specified during Brain() creation.");
        }

        FIS fis = FIS_Extra.load(this.getClass(), this.fuzzyDriverPath, false);
        FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();

        if( !didShowGraphs ){
            fuzzyRuleSet.chart();
            didShowGraphs = true;
        }

        for (int i = 0; i < amountOfVariables; i++) {
            fuzzyRuleSet.setVariable(variableNames[i], variableValues[i]);
        }

        fuzzyRuleSet.evaluate();

        Variable variable = fuzzyRuleSet.getVariable(resultVariableName);

        return variable.getLatestDefuzzifiedValue();
    }
}
