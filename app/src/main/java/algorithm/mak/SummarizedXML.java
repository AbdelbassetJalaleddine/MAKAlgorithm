package algorithm.mak;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.ArrayList;


@Root(name = "SummarizedInfo")
public class SummarizedXML {

    @ElementList(name = "Update" , required = false)
    private ArrayList<Update> updateList;

    @ElementList(name = "Insert" , required = false)
    private  ArrayList<Insert> insertList;

    @ElementList(name = "Delete", required = false)
    private  ArrayList<Delete> deleteList;


    @Attribute
    private String fromString;
    @Attribute
    private String toString;

    @Element
    private double EditDistance;
    @Element
    private double Similarity;

    @Attribute
    private int weightofInsert;
    @Attribute
    private int weightofDelete;
    @Attribute
    private int weightofUpdate;


    public void addUpdate(Update update) {
        try {
            if (updateList == null) {
                updateList = new ArrayList<Update>();
            }
            updateList.add(update);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addInsert(Insert insert) {
        try {
            if (insertList == null) {
                insertList = new ArrayList<Insert>();
            }
            insertList.add(insert);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDelete(Delete delete) {
        try {
            if (deleteList == null) {
                deleteList = new ArrayList<Delete>();
            }
            deleteList.add(delete);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getFromString() {
        return fromString;
    }

    public void setFromString(String fromString) {
        this.fromString = fromString;
    }
    public String getToString() {
        return toString;
    }

    public void setToString(String toString) {
        this.toString = toString;
    }

    public double getEditDistance() {
        return EditDistance;
    }

    public void setEditDistance(double EditDistance) {
        this.EditDistance = EditDistance;
    }
    public double getSimilarity() {
        return Similarity;
    }

    public void setSimilarity(double Similarity) {
        this.Similarity = Similarity;
    }


    public int getWeightofInsert() {
        return weightofInsert;
    }

    public void setWeightofInsert(int weightofInsert) {
        this.weightofInsert = weightofInsert;
    }

    public int getWeightofDelete() {
        return weightofDelete;
    }

    public void setWeightofDelete(int weightofDelete) {
        this.weightofDelete = weightofDelete;
    }

    public int getWeightofUpdate() {
        return weightofUpdate;
    }

    public void setWeightofUpdate(int weightofUpdate) {
        this.weightofUpdate = weightofUpdate;
    }
}