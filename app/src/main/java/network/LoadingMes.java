package network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zy on 16-1-20.
 */
public class LoadingMes implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.allLength);
        dest.writeLong(this.curLength);
        dest.writeString(this.symbol);
        dest.writeString(this.hashCode);
    }

    public LoadingMes() {
    }

    protected LoadingMes(Parcel in) {
        this.allLength = in.readLong();
        this.curLength = in.readLong();
        this.symbol = in.readString();
        this.hashCode = in.readString();
    }

    public static final Parcelable.Creator<LoadingMes> CREATOR = new Parcelable.Creator<LoadingMes>() {
        public LoadingMes createFromParcel(Parcel source) {
            return new LoadingMes(source);
        }

        public LoadingMes[] newArray(int size) {
            return new LoadingMes[size];
        }
    };
}
