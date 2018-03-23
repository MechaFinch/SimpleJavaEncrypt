/*Alex Pickering
 *3/20/2018
 *
 *Encryptor v2
 *Should be able to encrypt more than text this time
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.*;

public class Encryptor2{
    static String input = "";
    static File in;
    static File out;
    static int inputI = 0;
    static Cipher cipher;
    public static void main(String[] args) throws IOException {
        try{
            cipher = Cipher.getInstance("AES");
        } catch(NoSuchAlgorithmException e){

        } catch(NoSuchPaddingException e){

        }

        while(true)	{
            input = AR.readString("Please input the file path of the file to encrypt/decrypt.", "Please input a path.");

            in = new File(input);
            out	= new File(input);

            if(!in.exists()) {
                System.out.println("Please input an existing file");
                continue;
            }

            input = AR.readString("Please input the password to the file. Be careful, if you get it wrong the contents are most likely lost.", "Please input a string.");

            inputI = AR.readStringSI(new String[]{"encrypt", "decrypt"}, "Would you like to encrypt or decrypt the file?", "Please input 'encrypt' or 'decrypt'", "Please input 'encrypt' or 'decrypt'");

            try{
                if(inputI == 0){
                    enc();
                } else {
                    dec();
                }
            } catch(NoSuchAlgorithmException e){
                System.out.println("NoSuchAlgorithmException ocurred.");
            } catch(InvalidKeyException e){
                System.out.println("InvalidKetExceptione ocurred.");
            } catch(IllegalBlockSizeException e){
                System.out.println("IllegalBlockSizeException ocurred.");
            } catch(BadPaddingException e){
                System.out.println("BadPaddingException ocurred.");
            }

            if(!AR.readBooleanYN("Would you like to do anything else?", "Please input yes or no.")) System.exit(0);
        }
    }

    static void enc() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException{
        byte[] k = (input).getBytes("UTF-8");
        k = MessageDigest.getInstance("SHA-1").digest(k);
        k = Arrays.copyOf(k, 16);
        SecretKeySpec sk = new SecretKeySpec(k, "AES");

        if(in.isDirectory()){
            File[] inputFiles = getFolderFiles(in);
            for(int i = 0; i < inputFiles.length; i++){
                cipher.init(Cipher.ENCRYPT_MODE, sk);
                if(i == inputFiles.length - 1){
                    byte[] data = readFile(inputFiles[i]);
                    byte[] encData = cipher.doFinal(data);
                    writeFile(inputFiles[i], encData);
                } else {
                    byte[] data = readFile(inputFiles[i]);
                    byte[] encData = cipher.doFinal(data);
                    writeFile(inputFiles[i], encData);
                }
            }
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, sk);
            byte[] data = readFile(in);
            byte[] encData = cipher.doFinal(data);
            writeFile(out, encData);
        }
    }

    static void dec() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException{
        byte[] k = (input).getBytes("UTF-8");
        k = MessageDigest.getInstance("SHA-1").digest(k);
        k = Arrays.copyOf(k, 16);
        SecretKeySpec sk = new SecretKeySpec(k, "AES");

        if(in.isDirectory()){
            File[] inputFiles = getFolderFiles(in);
            for(int i = 0; i < inputFiles.length; i++){
                cipher.init(Cipher.DECRYPT_MODE, sk);
                if(i == inputFiles.length - 1){
                    byte[] encData = readFile(inputFiles[i]);
                    byte[] decData = cipher.doFinal(encData);
                    writeFile(inputFiles[i], decData);
                } else {
                    byte[] encData = readFile(inputFiles[i]);
                    byte[] decData = cipher.doFinal(encData);
                    writeFile(inputFiles[i], decData);
                }
            }
        } else {
            cipher.init(Cipher.DECRYPT_MODE, sk);
            byte[] encData = readFile(in);
            byte[] decData = cipher.doFinal(encData);
            writeFile(out, decData);
        }
    }

    static File[] getFolderFiles(File f){
        ArrayList<File> files = new ArrayList<File>();

        for(File fileEntry : f.listFiles()){
            if(fileEntry.isDirectory()){
                File[] ft = getFolderFiles(fileEntry);
                files.addAll(Arrays.asList(ft));
            } else {
                files.add(fileEntry);
            }
        }

        return files.toArray(new File[files.size()]);
    }

    static byte[] readFile(File f) throws IOException{
        return Files.readAllBytes(Paths.get(f.getPath()));
    }

    static void writeFile(File f, byte[] d) throws IOException{
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(d);
        fos.close();
    }
}