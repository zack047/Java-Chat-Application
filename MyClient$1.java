import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;


class MyClient$1 extends FocusAdapter {
    @Override
    public void focusGained(final FocusEvent focusEvent) {
        this.this$0.txtMessage.selectAll();
    }
}