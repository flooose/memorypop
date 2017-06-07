package de.jjff.flooose.memorypop;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chris on 02.01.17.
 */
public class DictionaryEntry implements Parcelable {
    public String mWord;
    public String mDefinition;

    public DictionaryEntry() {    }
    public DictionaryEntry(String word, String definition) {
        this.mWord = word;
        this.mDefinition = definition;
    }

    public DictionaryEntry(Parcel parcel) {
        this.mWord = parcel.readString();
        this.mDefinition = parcel.readString();
    }

    public static final Parcelable.Creator<DictionaryEntry> CREATOR = new Parcelable.Creator<DictionaryEntry>() {
        public DictionaryEntry createFromParcel(Parcel pc) {
            return new DictionaryEntry(pc);
        }
        public DictionaryEntry[] newArray(int size) {
            return new DictionaryEntry[size];
        }
    };

    public void setmWord(String word) {
        this.mWord = word;
    }

    public void setmDefinition(String definition) {
        this.mDefinition = definition;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DictionaryEntry && ((DictionaryEntry) obj).mWord.equalsIgnoreCase(mWord);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mWord);
        parcel.writeString(mDefinition);
    }
}
