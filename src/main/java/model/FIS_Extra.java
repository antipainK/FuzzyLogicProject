package model;

import antlr.CommonAST;
import antlr.collections.AST;
import antlr.debug.misc.ASTFrame;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.fcl.FCLLexer;
import net.sourceforge.jFuzzyLogic.fcl.FCLParser;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

public class FIS_Extra{
    public static boolean debug = false;
    HashMap<String, FuzzyRuleSet> ruleSets = new HashMap();

    private static net.sourceforge.jFuzzyLogic.FIS createFromLexer(FCLLexer lexer, boolean verbose) {
        net.sourceforge.jFuzzyLogic.FIS fis = new net.sourceforge.jFuzzyLogic.FIS();

        try {
            FCLParser parser = new FCLParser(lexer);
            parser.fcl();
            CommonAST parseTree = (CommonAST)parser.getAST();
            if (parseTree == null) {
                System.err.println("Can't create FIS");
                return null;
            } else {
                Gpr.debug(verbose, "Tree: " + parseTree.toStringTree());

                for(Object child = parseTree; child != null; child = ((AST)child).getNextSibling()) {
                    FuzzyRuleSet ruleSet = new FuzzyRuleSet();
                    String ruleSetName = ruleSet.fclTree((AST)child);
                    Gpr.debug(debug, "Ruleset Name: '" + ruleSetName + "'");
                    fis.addFuzzyRuleSet(ruleSetName, ruleSet);
                    if (verbose) {
                        ASTFrame astFrame = new ASTFrame("Tree", parseTree);
                        astFrame.setVisible(true);
                    }
                }

                return fis;
            }
        } catch (Exception var9) {
            Gpr.debug("Exception: " + var9);
            var9.printStackTrace();
            return null;
        }
    }

    public static net.sourceforge.jFuzzyLogic.FIS createFromString(String fclDefinition, boolean verbose) {
        StringReader stringReader = new StringReader(fclDefinition);
        FCLLexer lexer = new FCLLexer(stringReader);
        return createFromLexer(lexer, verbose);
    }

    public static net.sourceforge.jFuzzyLogic.FIS load(String fileName) {
        return load(fileName, false);
    }

    public static net.sourceforge.jFuzzyLogic.FIS load(String fileName, boolean verbose) {
        FCLLexer lexer;
        try {
            lexer = new FCLLexer(new FileInputStream(fileName));
        } catch (FileNotFoundException var4) {
            System.err.println("File '" + fileName + "' not found ");
            return null;
        }

        return createFromLexer(lexer, verbose);
    }

    public static net.sourceforge.jFuzzyLogic.FIS load(Class<?> brainClass, String fileName, boolean verbose) {
        FCLLexer lexer;
        lexer = new FCLLexer(brainClass.getResourceAsStream(fileName));

        return createFromLexer(lexer, verbose);
    }

    public FIS_Extra() {
    }

    /*public net.sourceforge.jFuzzyLogic.FIS addFuzzyRuleSet(String ruleSetName, FuzzyRuleSet ruleSet) {
        this.ruleSets.put(ruleSetName, ruleSet);
        return this;
    }*/

    public FuzzyRuleSet getFuzzyRuleSet() {
        return this.getFuzzyRuleSet((String)null);
    }

    public FuzzyRuleSet getFuzzyRuleSet(String ruleSetName) {
        if (ruleSetName == null) {
            ruleSetName = (String)this.ruleSets.keySet().iterator().next();
        }

        return (FuzzyRuleSet)this.ruleSets.get(ruleSetName);
    }

    public String toString() {
        return this.toString(false);
    }

    private String toString(boolean useFCL) {
        StringBuffer out = new StringBuffer();
        ArrayList al = new ArrayList((Collection)this.ruleSets.keySet());
        Collections.sort(al);
        Iterator it = al.iterator();

        while(it.hasNext()) {
            String ruleSetName = (String)it.next();
            FuzzyRuleSet ruleSet = this.getFuzzyRuleSet(ruleSetName);
            if (useFCL) {
                out.append(ruleSet.toStringFCL());
            } else {
                out.append(ruleSet.toString());
            }
        }

        return out.toString();
    }

    public String toStringFCL() {
        return this.toString(true);
    }
}
