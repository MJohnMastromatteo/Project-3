public class ListMan {

    //
    // Public
    //
    public ListMan() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLength() {
        return this.length;
    }

    public ListItem getHead() {
        return head;
    }
    public void setHead(ListItem head) {
        this.head = head;
    }

    public ListItem getLast() {
        return last;
    }
    public void setLast(ListItem last) {
        this.last = last;
    }

    public void add(ListItem item) {
        // System.out.println("adding " + item.toString());
        if (this.head == null) {
            // The list is empty.
            this.head = item;
            this.last = item;
        } else {
            // The list is NOT empty.
            this.last.setNext(item);
            this.last = item;
        }
        this.length = this.length + 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ListMan: name=" + this.name + " desc=" + this.desc + "] List Items:\n");
        ListItem currentItem = this.head;
        while (currentItem != null) {
            sb.append(currentItem.toString());
            sb.append("\n");
            currentItem = currentItem.getNext();
        }
        return sb.toString();
    }

    //
    // Private
    //
    private String name;
    private String desc;
    private int length = 0;
    private ListItem head = null;
    private ListItem last = null;
}
