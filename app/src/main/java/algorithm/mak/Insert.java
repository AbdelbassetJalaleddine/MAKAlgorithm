package algorithm.mak;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

public class Insert {

    @Element
    private int source;
    @Element
    private String value;
    @Element
    private int destination;
    @Attribute
    private int order;



    // If you like the variable name, e.g. "name", you can easily change this
    // name for your XML-Output:

    public int getSource() {
        return source;
    }

    public String getValue() {
        return value;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}