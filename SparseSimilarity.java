
import java.util.*;
import java.util.Map.*;
import sparsesimilarity.*;

class PairOfDocs
{
    public int doc_1;
    public int doc_2;

    public PairOfDocs(int doc_1, int doc_2)
    {
        this.doc_1 = doc_1;
        this.doc_2 = doc_2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof PairOfDocs)
        {
            PairOfDocs p = (PairOfDocs) o;
            return p.doc_1 == doc_1 && p.doc_2 == doc_2;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (doc_1 * 31) ^ doc_2;
    }
}

class PerDocument
{
    private ArrayList<Integer> words_in_doc;
    private int document_id;

    public PerDocument(int document_id, ArrayList<Integer> words_in_doc)
    {
        this.document_id = document_id;
        this.words_in_doc = words_in_doc;
    }

    public ArrayList<Integer> get_words()
    {
        return words_in_doc;
    }

    public int get_id()
    {
        return document_id;
    }

    public int size_of_doc()
    {
        return words_in_doc == null ? 0 : words_in_doc.size();
    }
}

public class SparseSimilarity
{
    public static HashMap<PairOfDocs, Double> find_similarity(HashMap<Integer, PerDocument> docs)
    {
        /* To find which pairs of documents will have similarity */
        SparseFunctions<Integer, Integer> compare_word_id = group_of_words(docs); 	
        
        /* Compute the size of intersection between each pair of documents */
        HashMap<PairOfDocs, Double> similarity = find_intersection(compare_word_id);		
        value_of_similarity(docs, similarity);
        return similarity;
    }

    /* Create a hashtable which helps to find which pairs of documents will have similarity
     * Iterate through each list of documents and
     * then each word within that list,
     * finding the repition of each element in different documents*/

    public static SparseFunctions<Integer, Integer> group_of_words(HashMap<Integer, PerDocument> documents)
    {
        SparseFunctions<Integer, Integer> compare_word_id = new SparseFunctions<Integer, Integer>();

        for (PerDocument doc : documents.values())
        {
            ArrayList<Integer> words = doc.get_words();
            for (int word : words)
            {
                compare_word_id.put(word, doc.get_id());
            }
        }

        return compare_word_id;
    }

	/* Compute the size of intersection between each pair of documents.
     * Iterate through each list of documents and
     * then each pair within that list,
     * incrementing the intersection of each page. */

	public static HashMap<PairOfDocs, Double> find_intersection(SparseFunctions<Integer, Integer> compare_word_id)
    {
        HashMap<PairOfDocs, Double> similarity = new HashMap<PairOfDocs, Double>();
        Set<Integer> words = compare_word_id.keySet();
        for (int word : words)
        {
            ArrayList<Integer> docs = compare_word_id.get(word);
            Collections.sort(docs);

            for (int i = 0; i < docs.size(); i++)
            {
                for (int j = i + 1; j < docs.size(); j++)
                {
                    increase(similarity, docs.get(i), docs.get(j));
                }
            }
        } 
        
        return similarity;
    }

    /* Passing a new empty hashtable as argument
     * Check if the pair is already present
     * If not present add that pair and initialize the intersection to 1
     * If the pair is already present increment the intersection size of the document pair. */

	public static void increase(HashMap<PairOfDocs, Double> similarities, int doc1, int doc2)
    {
        PairOfDocs pair = new PairOfDocs(doc1, doc2);

        if (!similarities.containsKey(pair))
        {
            similarities.put(pair, 1.0);
        }
        else
        {
            similarities.put(pair, similarities.get(pair) + 1);
        }
    }

    /* To find the value of the simalarity in the documents
     * Get key from the pair
     * Get the value corresponding to the keys
     * Computing the union from the intersection
     * By the fomula, computing the similarity value */

    public static void value_of_similarity(HashMap<Integer, PerDocument> documents, HashMap<PairOfDocs, Double> similarities)
    {
        for (Entry<PairOfDocs, Double> entry : similarities.entrySet())
        {
            PairOfDocs pair = entry.getKey();
            Double intersection = entry.getValue();
            PerDocument doc1 = documents.get(pair.doc_1);
            PerDocument doc2 = documents.get(pair.doc_2);

            double union = (double) doc1.size_of_doc() + doc2.size_of_doc() - intersection;

            entry.setValue(intersection / union);
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n\nInput the number of documents");
        int num_of_docs = sc.nextInt();


        System.out.println("\nInput:");

        HashMap<Integer, PerDocument> documents = new HashMap<Integer, PerDocument>();

        /* Getting input from user */
        for (int i = 0; i < num_of_docs; i++)
        {
            System.out.print("\nEnter the Document ID: ");
            int id = sc.nextInt();

            System.out.print("Input the size of document: ");
            int size_of_doc = sc.nextInt();
            int[] array = new int[size_of_doc];

            System.out.print("Enter the words in the Document: ");
            for (int j = 0; j < size_of_doc; j++)
            {
                array[j] = sc.nextInt();
            }

            ArrayList<Integer> unique_words = new ArrayList<Integer>();
            for(Integer k:array)
            {
                unique_words.add(k);
            }

            System.out.println("\n" + id + ": " + unique_words.toString());

            PerDocument doc = new PerDocument(id, unique_words);
            documents.put(id, doc);
        }

        HashMap<PairOfDocs, Double> similarity = find_similarity(documents);

        System.out.println("\n\nOutput:\n");
        System.out.println("ID1, " + " ID2 " + "    :  " + " SIMILARITY");

        /* Displaying the Similarities */
        for (Entry<PairOfDocs, Double> result : similarity.entrySet())
        {
            PairOfDocs pair = result.getKey();
            Double sim = result.getValue();
            System.out.println(pair.doc_1 + ",   " + pair.doc_2 + "      :  " + " " + sim);
        }

        sc.close();
    }

}