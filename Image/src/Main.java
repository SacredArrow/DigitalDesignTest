import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static BufferedImage image;
    private static ArrayList<ArrayList<Integer>> checked;
    private static ArrayList<Integer> lengths;

    private static void searchHorizontal(int i, int j) {
        int cnt = 0;
        int i_left = i;
        int i_right = i;
        while (i_right != image.getWidth() - 1 && image.getRGB(i_right + 1, j) == image.getRGB(i, j)) {
            cnt++;

            checked.get(i_right + 1).set(j, - 1);
            i_right++;
        }
        while (i_left != 0 && image.getRGB(i_left - 1, j) == image.getRGB(i, j)) {
            cnt++;
            checked.get(i_left - 1).set(j, - 1);
            i_left--;
        }
        lengths.add(cnt);
    }

    private static void searchVertical(int i, int j) {
        int cnt = 1;
        int j_up = j;
        int j_down = j;
        while (j_down != image.getHeight() - 1 && image.getRGB(i, j_down + 1) == image.getRGB(i, j)) {
            cnt++;
            checked.get(i).set(j_down + 1, - 1);
            j_down++;
        }

        while (j_up != 0 && image.getRGB(i, j_up - 1) == image.getRGB(i, j)) {
            cnt++;
            checked.get(i).set(j_up - 1, - 1);
            j_up--;
        }
        lengths.add(cnt);


    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the picture (relative to the project directory):");
        String path = scanner.nextLine();
        File imageFile = new File(path);
        try {
            image = ImageIO.read(imageFile);
            checked = new ArrayList<>(); //Creating array for storing already counted pixels
            for (int i = 0; i < image.getWidth(); i++) {
                ArrayList<Integer> tmp = new ArrayList<>();
                for (int j = 0; j < image.getHeight(); j++) {
                    tmp.add(0);
                }
                checked.add(tmp);
            }
            lengths = new ArrayList<>();
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    //Checking all the pixels for being not white
                    /* Pixels on crossings of 2 one-colored lines are counted twice('cause they belong to two lines,
                     otherwise they would split the line, although there is no reason not to see it as whole
                    */
                    if (image.getRGB(i, j) != (Color.WHITE.getRGB()) && checked.get(i).get(j) == 0) {
                        boolean flag = true;
                        if ((j != image.getHeight() - 1 && image.getRGB(i, j + 1) == image.getRGB(i, j)) || (j != 0 && image.getRGB(i, j - 1) == image.getRGB(i, j))) {
                            searchVertical(i, j);
                            flag = false;
                        }
                        if ((i != image.getWidth() - 1 && image.getRGB(i + 1, j) == image.getRGB(i, j)) || (i != 0 && image.getRGB(i - 1, j) == image.getRGB(i, j))) {
                            searchHorizontal(i, j);
                            flag = false;
                        }
                        if (flag) {
                            lengths.add(1);
                        }
                    }
                }
            }

            System.out.println("Number of lines: " + lengths.size());
            System.out.print("Lines have lengths of: ");
            int sum = 0;
            for (Integer i : lengths) {
                sum += i;
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println("Total length: " + sum); //As I mentioned, crossings of 2 one-colored lines are counted twice.
        } catch (IOException e) {
            System.out.println("File cannot be found!");
        }

    }


}
