package mm.pndaza.mahabuddhavan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    int id;
    String name;
    int volume;
    int page;

    public Note(int id, String name, int volume, int page) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.page = page;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVolume() {
        return volume;
    }

    public int getPage() {
        return page;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        name = in.readString();
        volume = in.readInt();
        page = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(volume);
        dest.writeInt(page);
    }
}

