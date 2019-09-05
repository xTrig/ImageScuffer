package com.trig.imagescuffer;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.BitSet;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Steven on 2019-09-04.
 */
public class ImageScuffer {


    
    private File srcImage, dstImage;
    private Random rand = new Random();

    //Static variables used by main() during CLI
    private static File inImage;
    private static File outImage;

    public static void main(String[] args) {
        if(args.length > 0) {
            String srcImagePath = args[0]; //No space file path
            File srcImage = new File(srcImagePath);
            String dstImagePath = args[1];
            File dstImage = new File(dstImagePath);
            ImageScuffer ig = new ImageScuffer(srcImage, dstImage);
            ig.scuff();
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter source image path: ");
            inImage = new File(scanner.nextLine());
            if(!inImage.exists()) {
                System.out.println("Source file specified does not exist!");
                System.exit(0);
            }
            if(inImage.isDirectory()) {
                System.out.println("Specified path must be a file, not a directory.");
                System.exit(0);
            }
            String ext = FilenameUtils.getExtension(inImage.getName());
            outImage = new File("out." + ext);
            System.out.println("Making output file: " + outImage.getPath());
            try {
                if(inImage.getCanonicalPath().equals(outImage.getCanonicalPath())) {
                    System.out.println("Input image cannot be the same as output image");
                    System.exit(0);
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            ImageScuffer ig = new ImageScuffer(inImage, outImage);
            ig.scuff();
        }
    }

    /**
     * Creates an ImageScuffer with the specified input and output paths
     * @param srcImage
     * @param dstImage
     */
    public ImageScuffer(File srcImage, File dstImage) {
        this.srcImage = srcImage;
        this.dstImage = dstImage;
    }

    /**
     * Flips random bits from the source image and writes them to the output image
     */
    public void scuff() {
        System.out.println("Starting to scuff file...");
        try {
            if(!dstImage.exists()) { //Create the destination image
                System.out.println("Created destination image!");
                dstImage.createNewFile();
            }
            FileInputStream fis = new FileInputStream(srcImage); //Create a FileInputStream to read in the src file
            FileOutputStream fos = new FileOutputStream(dstImage); //Create a FileOutputStream to write the new bytes to
            int bytesPerBuf = 32; //1 every bytesPerBuf bytes will be modified to prevent too much image corruption
            byte[] inBuf = new byte[bytesPerBuf]; //Read in the specified amount of bits
            //Read each image byte one at a time and modify it
            int byteCounter = 0;
            while(fis.read(inBuf) != -1) { //Keep reading the src file until we reach the end of the file
                byteCounter+= bytesPerBuf; //Increment the byte counter
                if(byteCounter % 2048 == 0) { //Every 2K bytes will have modified data
                    byte[] newBytes = rewriteBits(inBuf); //Flip a random bit
                    fos.write(newBytes); //Write the new image data
                } else {
                    fos.write(inBuf); //Write the original file data if the data shouldn't be modified
                }

            }
            fos.close(); //Close the outputstream
            fis.close(); //Close the inputstream
            System.out.println("Done making garbage image!");

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Flips a random bit in the byte[]
     * @param buf The buffer to modify
     * @return The new byte[] with modified vals
     */
    private byte[] rewriteBits(byte[] buf) {
        BitSet bits = BitSet.valueOf(buf);
        bits.flip(rand.nextInt(bits.length()));
        return bits.toByteArray();
    }
}
