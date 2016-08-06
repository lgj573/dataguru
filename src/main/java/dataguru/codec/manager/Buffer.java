package dataguru.codec.manager;

public class Buffer {
    StringBuffer sb;

    public Buffer() {
        sb = new StringBuffer();
    }

    public void append(String content) {
        sb.append(content);
    }
    public String getLine(){
        String line = sb.toString();
        refresh();
        return line;
    }
    public void refresh(){
        sb.delete(0,sb.length());
    }
}
