public class Main {

    public static void main(String[] args) {
        if( args.length < 1){
            args = new String[1];
            args[0] = "src/main/resources/fuzzyDriver.fcl";
        }
        start(args);
    }

    private static void start(String[] args){

        try {

            String pathToDriver = args[0];
            Game.main(new String[]{pathToDriver});

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
