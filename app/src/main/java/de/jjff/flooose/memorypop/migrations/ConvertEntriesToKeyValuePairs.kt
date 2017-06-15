package de.jjff.flooose.memorypop.migrations

import com.google.firebase.database.*
import de.jjff.flooose.memorypop.DictionaryEntry

class ConvertEntriesToKeyValuePairs (mCurrentUser: DatabaseReference, dictionarReference: DatabaseReference) {
    var userRef: DatabaseReference = mCurrentUser;
    var dictionaryReference: DatabaseReference = dictionarReference;

    fun migrate() {
        val dictionary = userRef.child("dictionary")
        dictionary.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    var count = 0
                    val dictionaryEntriesIterator = dataSnapshot.children.iterator()
                    var currentEntryDS = dictionaryEntriesIterator.next()
                    while (dictionaryEntriesIterator.hasNext()) {
                        val entry = currentEntryDS.getValue(DictionaryEntry::class.java)
                        dictionaryReference.push().setValue(entry);
                        currentEntryDS = dictionaryEntriesIterator.next()
                        count++
                    }
                    dictionary.removeValue()
                    dictionary.removeEventListener(this);
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}