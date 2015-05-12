package game.ui;

/**
This class does not appear to be used in the Linux C++ code
public class CRectangle extends CUIElement
{
public void Draw(CGame game, SInt2 translation) {
Pixmap Pixmap = game.Rendering().DWorkingBufferPixmap;
GdkGC DrawingContext;
if(IsPressed()) {
DrawingContext = game.DrawingArea().style.white_gc;
}
else if(IsSelected()) {
DrawingContext = game.DrawingArea().style.dark_gc[0];
}
else {
DrawingContext = game.DrawingArea().style.black_gc;
}
gdk_draw_rectangle(Pixmap, DrawingContext, 1, translation.DX, translation.DY, Size().DX, Size().DY);
super.Draw(game, new SInt2(translation));
}
public void Update(CGame game, SInt2 translation) {
super.Update(game, new SInt2(translation));
if(IsPressed()) {
Position(Position() + new SInt2(10, 0));
}
}
}
*/

