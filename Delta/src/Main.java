import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    // Sets for searching moved files
    private static HashSet<FileWithMD5> missingFiles;
    private static HashSet<FileWithMD5> newFiles;

    public static String checkSum(File f) { //Calculates MD5 using Apache Commons
        String md5 = "";
        try {
            FileInputStream fis = new FileInputStream(f);
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static void scanDir(File dir, Folder folder) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                Folder tmp = new Folder();
                folder.put(f.getName(), tmp);
                scanDir(f, tmp);
            } else {
                String md5 = checkSum(f);
                folder.files.put(f.getName(), md5);

            }
        }
    }

    public static void printFolder(Folder folder) { // Helper function for browsing read folder
        for (String fileName : folder.files.keySet()) {
            System.out.println(fileName);
        }
        for (String key : folder.keySet()) {
            System.out.println(key);
            printFolder(folder.get(key));
        }
    }

    @SuppressWarnings ("Duplicates")
    public static void findDeltaRecursively(Folder fold1, Folder fold2) { // Checks differences and handles them
        for (String file : fold2.files.keySet()) {
            if (! fold1.files.keySet().contains(file)) {
                boolean flag = true;
                for (FileWithMD5 f : missingFiles) {
                    if (f.getName().equals(file) && f.getMd5().equals(fold2.files.get(file))) {
                        System.out.println("Moved file " + f.getName());
                        missingFiles.remove(f);
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    newFiles.add(new FileWithMD5(file, fold2.files.get(file)));
                }
            }
        }
        for (String file : fold1.files.keySet()) {
            if (! fold2.files.keySet().contains(file)) {
                boolean flag = true;
                for (FileWithMD5 f : newFiles) {
                    if (f.getName().equals(file) && f.getMd5().equals(fold1.files.get(file))) {
                        System.out.println("Moved file " + f.getName());
                        newFiles.remove(f);
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    missingFiles.add(new FileWithMD5(file, fold1.files.get(file)));
                }
            }
        }
        for (String file : fold2.files.keySet()) {
            if (fold1.files.keySet().contains(file)) {
                if (! fold1.files.get(file).equals(fold2.files.get(file)))
                    System.out.println("Changed file " + file);
            }
        }
        for (String key : fold1.keySet()) {
            if (! fold2.containsKey(key)) {
                System.out.println("Deleted folder " + key);
            }
        }
        for (String key : fold2.keySet()) {
            if (! fold1.containsKey(key)) {
                System.out.println("Added folder " + key);
            }
        }
        for (String key : fold1.keySet()) {
            if (fold2.containsKey(key)) {
                findDeltaRecursively(fold1.get(key), fold2.get(key));
            }
        }
    }

    public static void findDelta(Folder fold1, Folder fold2) { // Finds moved files and then prints deleted/new
        findDeltaRecursively(fold1, fold2);
        for (FileWithMD5 file : missingFiles) {
            System.out.println("Deleted file " + file.getName());
        }
        for (FileWithMD5 file : newFiles) {
            System.out.println("Created file " + file.getName());
        }
    }

    public static void main(String[] args) {
        Folder startFolder = new Folder();
        missingFiles = new HashSet<>();
        newFiles = new HashSet<>();
        System.out.print("Enter the path to the directory (relative to the project directory):");
        Scanner scanner = new Scanner(System.in);
        String path1 = scanner.nextLine();
        System.out.print("Enter the path to the another directory (or change the previous one and press Enter:");
        String path2 = scanner.nextLine();
        if (path2.equals("")) {
            path2 = path1;
        }
        File actual = new File(path1);
        scanDir(actual, startFolder);
        Folder secondFolder = new Folder();
        actual = new File(path2);
        scanDir(actual, secondFolder);
        findDelta(startFolder, secondFolder);
    }
}
