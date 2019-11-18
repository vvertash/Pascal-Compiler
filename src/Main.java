public class Main {
    public static void main(String[] args) {
        args = new String[1];
        args[0] = "in.txt";

        if (args.length == 0) {
            System.out.println("ERROR: No input provided");
        } else {
            FilesTokenizer filesTokenizer = new FilesTokenizer(args);
            filesTokenizer.tokenizeFiles();
        }
    }


}
