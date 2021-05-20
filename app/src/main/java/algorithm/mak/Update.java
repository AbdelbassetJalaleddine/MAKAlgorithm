package algorithm.mak;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;



// arrange property/element order of xml element, this is Optional
public class Update {

    @Element
    private int source;
    @Element
    private int destination;
    @Attribute
    private int order;
    @Element
    private String value;

    // If you like the variable name, e.g. "name", you can easily change this
    // name for your XML-Output:
    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public void setSource(int source) {
        this.source = source;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}