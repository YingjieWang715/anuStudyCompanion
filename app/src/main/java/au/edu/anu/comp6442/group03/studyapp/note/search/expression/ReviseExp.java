package au.edu.anu.comp6442.group03.studyapp.note.search.expression;

import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.search.Token;
import au.edu.anu.comp6442.group03.studyapp.note.search.TokenType;

public class ReviseExp extends Exp {
    public ReviseExp(String value) {
        super(TokenType.REVISE, value);
    }

    @Override
    public Timestamp parse() {
        try {
            int daysToAdd = Integer.parseInt(this.value);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
            return new Timestamp(calendar.getTime());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Revise value must be a valid integer.");
        }
    }

    @Override
    public boolean match(NoteData note) {
        if (note.getReminder() != null){
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTime(new Date());
            Timestamp currentTime = new Timestamp(newCalendar.getTime());

            if (Long.compare(note.getReminder().getSeconds(), currentTime.getSeconds()) >= 0){
                if (Long.compare(note.getReminder().getSeconds(), this.parse().getSeconds()) != 0) {
                    return Long.compare(note.getReminder().getSeconds(), this.parse().getSeconds()) < 0;
                }
                else{
                    return Long.compare(note.getReminder().getNanoseconds(), this.parse().getNanoseconds()) <= 0;
                }
            }

        }
        return false;
    }


}
