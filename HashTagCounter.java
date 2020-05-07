package hashtagcounter;

import java.io.*;
import java.util.HashMap;

public class HashTagCounter {

    public static void main(String[] args)  {
	// write your code here



        try{
            int inp_length = args.length;

            if (inp_length < 1){
                throw new IncorrectArgumentsException("*** No Arguments Passed *** \n"+
                                                      "I/P Formats :-\n"+
                                                      "\t1].\tjava hashtagcounter.HashTagCounter <input_file> <output_file> \t(writes the program output into the output file specified)\n" +
                                                      "\t2].\tjava hashtagcounter.HashTagCounter <input_file>               \t(prints the output on the screen)\n"
                        );
            }
            else{
                FiboHeap fg = new FiboHeap();
                fg.hashMap = new HashMap<String, Node>();
                String str;

                /* reading from the input file */
                File filename = new File(args[0]);
                FileInputStream fstream = new FileInputStream(filename);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fstream));
                FileReader fileReader = new FileReader(filename);

                if (inp_length == 1){

                    while ((str = bufferedReader.readLine()) != null && !str.toUpperCase().equals("STOP")){
                        String[] strArr = str.split(" ");

                        // filter lines that do not contain a hashtag
                        if( str.indexOf("#") != -1 ){
                            // extract the hashtag name
                            String hashtag = strArr[0].substring(1);
                            int hashtagCount = Integer.parseInt(strArr[1]);

                            // check if the heap already contains the hashtag
                            if (fg.hashMap.containsKey(hashtag)){
                                // increase-key if the hashtag already exists
                                fg.increaseKey(fg.hashMap.get(hashtag).right, hashtagCount);
                            } else{
                                // if the hashtag does not exist, insert it into the fibonacci heap
                                Node ins = fg.insert(hashtagCount, hashtag);
                                Node pointer = new Node(-1, "dummy");
                                pointer.right = ins;
                                fg.hashMap.put(hashtag, pointer);
                            }
                        } else{
                            // call remove max
                            Integer remMax = Integer.parseInt(strArr[0]);
                            fg.removeNMaxes(remMax);
                        }
                    }
                    bufferedReader.close();

                } else{
                    // output file name is specified
                    String output_filename = args[1];

                    FileWriter fileWriter = new FileWriter(new File(output_filename), false);
                    fileWriter.write("");
                    fileWriter.flush();
                    fileWriter.close();


                    while ((str = bufferedReader.readLine()) != null && !str.toUpperCase().equals("STOP")){
                        String[] strArr = str.split(" ");

                        // filter lines that do not contain a hashtag
                        if( str.indexOf("#") != -1 ){
                            // extract the hashtag name
                            String hashtag = strArr[0].substring(1);
                            int hashtagCount = Integer.parseInt(strArr[1]);

                            // check if the heap already contains the hashtag
                            if (fg.hashMap.containsKey(hashtag)){
                                // increase-key if the hashtag already exists
                                fg.increaseKey(fg.hashMap.get(hashtag).right, hashtagCount);
                            } else{
                                // if the hashtag does not exist, insert it into the fibonacci heap
                                Node ins = fg.insert(hashtagCount, hashtag);
                                Node pointer = new Node(-1, "dummy");
                                pointer.right = ins;
                                fg.hashMap.put(hashtag, pointer);
                            }
                        } else{
                            // call remove max
                            Integer remMax = Integer.parseInt(strArr[0]);
                            fg.removeNMaxes(remMax, output_filename);
                        }
                    }
                    bufferedReader.close();

                }
            }


        }catch (IncorrectArgumentsException e){
            System.out.println("Incorrect Argument Exception occurred => "+e.getMessage());
        }
        catch (Exception e){
            System.out.println("Exception occured => "+e.getMessage());
        }

        // main ends
    }

    // class ends
}
