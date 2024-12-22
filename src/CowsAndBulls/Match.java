package CowsAndBulls;

public class Match {
    public int cows;
    public int bulls;
    private String seq;
    private String attemp;
    public int turnCount;

    public Match(String seq) {
        turnCount = 0;
        cows = 0;
        bulls = 0;
        this.seq = seq;
        //gameProcess();
    }

    public static boolean checkSeq(String num) {
        if (num.length() != 4) return false;
        if (!(num.chars().distinct().count() >= num.length())){
            return false;
        }
        return true;
    }

    private String consoleInput() {
        //TODO
        //change it
        String newSeq = "Error";
        System.out.println("Введите ваше значение:");
        try {
            newSeq = System.console().readLine();
        } catch (Exception e) {
            System.out.println("Некорректное значение:");
        }
        if (!checkSeq(newSeq)) return "Error";
        return newSeq;
    }

    public void checkAttemp(String att) {
        bulls = 0;
        cows = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (seq.charAt(i) == att.charAt(j) && i == j){
                    bulls++;
                }
                else if (seq.charAt(i) == att.charAt(j)) {
                    cows++;
                }
            }
        }
    }

    private void consoleOutput(String mes) {
        //TODO
        //change it
        if (mes == "next") {
            System.out.println("Количстево быков = " + bulls + "\n" +
                    "Количество коров = " + cows + "\n");
        }
        else if (mes == "finish") {
            System.out.println("Поздравляю, вы выиграли за " + turnCount +
                    " ходов!\n");
        }
    }

    private void gameProcess() {
        while (true) {
            attemp = consoleInput();
            checkAttemp(attemp);
            turnCount++;
            if (bulls == 4) {
                consoleOutput("finish");////////////////
                return;
            }
            consoleOutput("next");/////////////////////
        }
    }
}
