package algorithm.mak;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


public class Delete {

    @Element
    private String value;
    @Element
    private int destination;
    @Attribute
    private int order;


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    public String getValue() {
        return value;
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
}
