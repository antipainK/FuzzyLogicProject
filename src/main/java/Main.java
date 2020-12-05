import model.Brain;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

public class Main {

    public static void main(String[] args) {
        if( args.length < 3){
            args = new String[3];
            args[0] = "src/main/resources/fuzzyDriver.fcl";
            args[1] = "90";
            args[2] = "12";
        }
        start(args);
    }

    private static void start(String[] args){

        try {
            String pathToDriver = args[0];
            int poziomNatezenia = Integer.parseInt(args[1]);
            int poraDnia = Integer.parseInt(args[2]);


            Brain temp = new Brain(pathToDriver, 2);
            String result = temp.calculateResult(new String[]{"poziom_natezenia", "pora_dnia"}, new Double[]{(double)poziomNatezenia, (double)poraDnia}, "zmiana_natezenia");
            System.out.println(result);




            /*
            FIS fis = FIS.load(pathToDriver,false);
//wyswietl wykresy funkcji fuzyfikacji i defuzyfikacji
            FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();
            //fuzzyRuleSet.chart();
//zadaj wartosci wejsciowe
            fuzzyRuleSet.setVariable("poziom_natezenia", poziomNatezenia);
            fuzzyRuleSet.setVariable("pora_dnia", poraDnia);
//logika sterownika
            fuzzyRuleSet.evaluate();
//graficzna prezentacja wyjscia
            //System.out.println(fuzzyRuleSet.getVariable("zmiana_natezenia").getLinguisticTerm("podglosnij"));
            System.out.println(fuzzyRuleSet);
            fuzzyRuleSet.getVariable("zmiana_natezenia").chartDefuzzifier(true);
//System.out.println(fuzzyRuleSet);
            System.out.println("######################\n\n\n\n");
            */





        } catch (Exception ex) {
            System.out.println(ex.toString());
        }


    }

}
