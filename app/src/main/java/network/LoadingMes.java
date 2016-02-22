package network;

/**
 * Created by zy on 16-1-20.
 */
public class LoadingMes {
    private long allLength;
    private long curLength;

    private String symbol;
    private String hashCode;

    public long getAllLength() {
        return allLength;
    }

    public synchronized void setAllLength(long allLength) {
        this.allLength = allLength;
    }

    public long getCurLength() {
        return curLength;
    }

    public synchronized void setCurLength(long curLength) {
        this.curLength = curLength;
    }

    public boolean isDone() {
        return curLength >= allLength;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getHashCode() {
        if (hashCode == null || hashCode.isEmpty()){
            return symbol;
        }
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
