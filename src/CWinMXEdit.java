/*
 * CWinMXEdit.java
 *
 * by Emulator
 *
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class CWinMXEdit extends CustomItem implements ItemCommandListener
{
    private String Label;
    private String Text;

    private int Width = 0;
    private int Height = 0;

    private boolean Wrap = true;
    private boolean EnableColours = true;

    private int backcolor; //Background Color       
    private int manych = 28; //Characters for line
    private int scroll = 11; //Line Scroll

    private int DistanceAppPref = 2;
    private int DistanceApp = 0;
    private int Distance = 14; //Distance beetween lines
    private int Distance2 = 2;
    private int Distance3 = 9; //Distance beetween Chars

    private Font font; //Font type

    private String LogLines[] = new String[100];
    private int loglines = 0;

    private int DistMap[] = new int[256];
    private int DistMap2[] = new int[256];
    private int Colours[] = new int[256];

    private int Margin = 70;

    private boolean DarkMode = true;

    public CWinMXEdit(String Label, String Text, int Width, int Height, boolean DarkMode)
    {
        super(Label);

        this.Label = Label;
        this.Text = Text;

        setDisplaySize(Width, Height, false);

        setColours(DarkMode, false);

        for (int i = 0; i < 256; i++)
        {
            DistMap[i] = 0;
            DistMap2[i] = Distance3;
        }

        DistMap[32] = -2;
        DistMap[33] = 2;
        DistMap[34] = 2;
        DistMap[39] = 2;
        DistMap[42] = 2;
        DistMap[44] = 2;
        DistMap[45] = 3;
        DistMap[46] = 2;
        DistMap[58] = 2;
        DistMap[59] = 2;
        DistMap[73] = 2;
        DistMap[94] = 4;
        DistMap[96] = 2;
        DistMap[105] = 3;
        DistMap[106] = 3;
        DistMap[108] = 2;
        DistMap[109] = -1;
        DistMap[112] = 1;
        DistMap[116] = 1;

        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);

        setItemCommandListener(this);
    }

    protected int getMinContentWidth()
    {
        return Width;
    }

    protected int getMinContentHeight()
    {
        return Height;
    }

    protected int getPrefContentWidth(int i)
    {
        return Width;
    }

    protected int getPrefContentHeight(int i)
    {
        return Height;
    }

    protected void paint(Graphics g, int i, int i0)
    {
        g.setColor(backcolor);
        g.fillRect(0, 0, Width, Height);

        DistanceApp = DistanceAppPref;

        String string = "";
        int len = 0;

        int col = 8;

        if (!EnableColours)
        {
            col = 1;
        }

        g.setFont(font);
        g.setColor(Colours[col]);

        for (int l = 0; l < loglines; l++)
        {
            string = LogLines[l];
            len = string.length();

            for (int d = 0, pos = 0; d < len; d++, pos++)
            {
                /*if ((d+2) < len)
                {
                    System.err.println("\"" + string.charAt(d) + "\" \"" + string.charAt(d+1) + "\"");
                }*/

                for (int c = 1; c < 256; c++) //WinMX Colour group from 1 to 255
                {
                    if ((d+2) < len)
                    {
                        if (string.charAt(d) == 3 && string.charAt(d+1) == c)
                        {
                            g.setColor(Colours[c]);

                            d += Distance2;
                        }
                    }
                }

                int n = ((int)string.charAt(d));

                if (n > 255)
                {
                    n = 255;
                }

                if (string.charAt(d) == '\n')
                {
                    continue;
                }

                int Dist = DistanceAppPref;

                if (pos > 0)
                {
                    Dist = (((pos * DistMap2[n]) + DistMap[n]));
                }

                if (EnableColours)
                {
                    //Draw Char
                    g.drawChar(string.charAt(d), Dist, DistanceApp, Graphics.LEFT | Graphics.TOP); //char, x, y, anchor
                }

                else
                {
                    //Draw String
                    g.drawString(string.substring(2, string.length()), DistanceAppPref, DistanceApp, Graphics.LEFT | Graphics.TOP); //char, x, y, anchor
                }
            }

            DistanceApp += Distance;
        }

        g.setColor(Colours[col]);
    }

    public void commandAction(Command command, Item item)
    {
        
    }

    public void setColours(boolean DarkMode, boolean Repaint)
    {
        if (DarkMode)
        {
            backcolor = 0x000000;
        }

        else
        {
            backcolor = 0xFFFFFF;
        }

        for (int i = 0; i < 256; i++)
        {
            Colours[i] = backcolor;
        }

        if (DarkMode)
        {
            Colours[0] = 0x000000;
            Colours[1] = 0xFFFFFF;
            Colours[2] = 0xffff00;
            Colours[3] = 0xff00ff;
            Colours[4] = 0x00FF00;
            Colours[5] = 0xff8080;
            Colours[6] = 0x00ffff;
            Colours[7] = 0x0000FF;
            Colours[8] = 0xFF0000;
            Colours[9] = 0x808080;
            Colours[50] = 0x0000FF;
            Colours[51] = 0x0080ff;
            Colours[52] = 0x00ffff;
            Colours[53] = 0x00ff80;
            Colours[54] = 0x00ff00;
            Colours[55] = 0x80ff00;
            Colours[56] = 0xffff00;
            Colours[57] = 0xff8000;
            Colours[58] = 0xFF0000;
            Colours[59] = 0xff0080;
            Colours[60] = 0xff00ff;
            Colours[61] = 0x8000ff;
            Colours[62] = 0xc0c0c0;
            Colours[63] = 0x404040;
            Colours[64] = 0x000080;
            Colours[65] = 0x004080;
            Colours[66] = 0x008080;
            Colours[67] = 0x008040;
            Colours[68] = 0x408000;
            Colours[69] = 0x406400;
            Colours[70] = 0x808000;
            Colours[71] = 0x804000;
            Colours[72] = 0x800000;
            Colours[73] = 0x800040;
            Colours[74] = 0x800080;
            Colours[75] = 0x400080;
            Colours[221] = 0xd0b652;
            Colours[237] = 0x18b952;
            Colours[253] = 0xee0000;
            Colours[254] = 0xFFFFFF;
            Colours[255] = 0xFFFFFF;
        }

        else
        {
            backcolor = 0xFFFFFF;

            Colours[0] = 0xFFFFFF;
            Colours[1] = 0x000000;
            Colours[2] = 0xFF0000;
            Colours[3] = 0xff00ff;
            Colours[4] = 0x008000;
            Colours[5] = 0x804000;
            Colours[6] = 0x00ffff;
            Colours[7] = 0x0000FF;
            Colours[8] = 0x800040;
            Colours[9] = 0x808080;
            Colours[50] = 0x0000FF;
            Colours[51] = 0x0080ff;
            Colours[52] = 0x00ffff;
            Colours[53] = 0x00ff80;
            Colours[54] = 0x00ff00;
            Colours[55] = 0x80ff00;
            Colours[56] = 0xffff00;
            Colours[57] = 0xff8000;
            Colours[58] = 0xFF0000;
            Colours[59] = 0xff0080;
            Colours[60] = 0xff00ff;
            Colours[61] = 0x8000ff;
            Colours[62] = 0xc0c0c0;
            Colours[63] = 0x404040;
            Colours[64] = 0x000080;
            Colours[65] = 0x004080;
            Colours[66] = 0x008080;
            Colours[67] = 0x008040;
            Colours[68] = 0x408000;
            Colours[69] = 0x406400;
            Colours[70] = 0x808000;
            Colours[71] = 0x804000;
            Colours[72] = 0x800000;
            Colours[73] = 0x800040;
            Colours[74] = 0x800080;
            Colours[75] = 0x400080;
            Colours[221] = 0xd0b652;
            Colours[237] = 0x18b952;
            Colours[253] = 0xee0000;
            Colours[254] = 0x000000;
            Colours[255] = 0x000000;
        }

        if (Repaint)
        {
            showNotify();
            repaint();
        }
    }

    public void setBackgroundColor(int RGB)
    {
        this.backcolor = RGB;

        repaint();
        showNotify();
    }

    public void setDisplaySize(int Width, int Height)
    {
        setDisplaySize(Width, Height, true);
    }

    private void setDisplaySize(int Width, int Height, boolean Repaint)
    {
        if (Width == 0)
        {
            Width = 240;
        }

        if (Height == 0)
        {
            Height = 320;
        }

        this.Width = Width;
        this.Height = Height - Margin;

        this.setPreferredSize(this.Width, this.Height);

        double widthdiv = 10;
        double heightdiv = 15;

        if (!EnableColours)
        {
            widthdiv = 9;
        }

        //Many Chars for Line Calculation
        manych = (int)((double)this.Width / (double)widthdiv);

        if (!EnableColours)
        {
            manych += 2;
        }

        //ScrollLimit Calculation
        scroll = (int)((double)this.Height / (double)heightdiv);

        if (Repaint)
        {
            showNotify();
            repaint();
        }
    }

    public void setScrollLimit(int ScrollLines)
    {
        this.scroll = ScrollLines;

        showNotify();
        repaint();
    }

    public void setLineLimit(int LineLimit)
    {
        this.manych = LineLimit;

        showNotify();
        repaint();
    }

    public void setFont(Font font)
    {
        this.font = font;

        showNotify();
        repaint();
    }

    public void setDistance(int dist, int dist2, int dist3, int dist4)
    {
        this.DistanceAppPref = dist;
        this.Distance = dist2;
        this.Distance2 = dist3;
        this.Distance3 = dist4;

        showNotify();
        repaint();
    }

    public void setWrap(boolean Wrap)
    {
        this.Wrap = Wrap;
    }

    public void setEnableColours(boolean EnableColours)
    {
        String Lines[] = new String[100];
        int Loglines = loglines;

        for (int l = 0; l < loglines; l++)
        {
            Lines[l] = LogLines[l];
        }

        clear();

        for (int l = 0; l < Loglines; l++)
        {
            appendString(Lines[l]);
        }

        this.EnableColours = EnableColours;

        setDisplaySize(Width, (Height + Margin), true);
    }

    public boolean getEnableColours()
    {
        return this.EnableColours;
    }

    public void appendString(String Text)
    {
        if (!Wrap)
        {
            NormalAppend(Text, true);
        }

        else
        {
            int len = Text.length();

            int c = -1;

            //Firs color check, if not put White
            if (!EnableColours)
            {
                if (len > 1)
                {
                    if (Text.charAt(0) == 3)
                    {
                        c = Text.charAt(1);
                    }
                }

                if (c < 0)
                {
                    c = 1;
                }
            }

            int lines = (len/manych) + 1;

            if (lines > 1)
            {
                boolean islast = false;

                //Text wrapping
                for (int i = 0, to = 0, to2 = manych; ; i++)
                {
                    int surplus = 0;

                    islast = false;

                    if (to2 > len)
                    {
                        to2 = len;

                        islast = true;
                    }

                    //Fix damn colour code truncation...
                    else if (EnableColours)
                    {
                        if (to2 <= (len-2))
                        {
                            char col[] = new char[3];

                            if (surplus == 0)
                            {
                                col[0] = Text.charAt(to2);
                                col[1] = Text.charAt(to2+1);
                                col[2] = '\0';

                                if (IsColour(col))
                                {
                                    surplus = 2;
                                    to2 += surplus;
                                }
                            }
                        }
                    }

                    if (to >= to2)
                    {
                        break;
                    }

                    int surplus2 = (GetColoursCount(Text.substring(to, to2))*2);
                    to2 += surplus2;

                    if (to2 > len)
                    {
                        to2 = len;

                        islast = true;
                    }

                    String tmp = Text.substring(to, to2);

                    if (!EnableColours)
                    {
                        tmp = CleanStringColours(tmp);
                        tmp = GetColor(c) + tmp;
                    }

                    NormalAppend(tmp, islast);

                    to = to2;
                    to2 += (manych + surplus);
                }
            }

            else
            {
                if (!EnableColours)
                {
                    Text = CleanStringColours(Text);
                    Text = GetColor(c) + Text;
                }

                NormalAppend(Text, true);
            }
        }

        showNotify();
        repaint();
    }

    private void Fresh()
    {
        if (loglines > scroll)
        {
            for (int x = 0; x < scroll; x++)
            {
                LogLines[x] = LogLines[x+1];
            }

            loglines--;
        }
    }

    private void NormalAppend(String Text, boolean islast)
    {
        LogLines[loglines] = Text;

        if (islast && EnableColours)
        {
            LogLines[loglines] += GetColor(8) + " ";
        }

        loglines++;

        Fresh();
    }

    public void clear()
    {
        loglines = 0;

        showNotify();
        repaint();
    }

    private boolean IsColour(char[] Text)
    {
        if (Text.length > 1)
        {
            if (Text[0] == 3)
            {
                for (int z = 0; z < 256; z++)
                {
                    if (Text[1] == (char)z)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int GetColoursCount(String Text)
    {
        int count = 0, len = Text.length();

        for (int i = 0; i < (len-1); i++)
        {
            if (Text.charAt(i) == 3)
            {
                for (int z = 0; z < 256; z++)
                {
                    if (Text.charAt(i+1) == (char)z)
                    {
                        count++;
                        i++;

                        break;
                    }
                }
            }
        }

        return count;
    }

    private String CleanString(String Text)
    {
        String Tmp = "";

        for (int i = 0; i < Text.length(); i++)
        {
            if (Text.charAt(i) != 0)
            {
                Tmp += Text.charAt(i);
            }
        }

        return Tmp;
    }

    private String CleanStringColours(String Text)
    {
        String Tmp = Text;

        for (int i = 0; i < 256; i++)
        {
            Tmp = Utils.replaceAll(Tmp, (char)3 + "" + (char)i, "");
        }

        return Tmp;
    }

    public String GetColor(int c)
    {
        return (char)3 + "" + (char)c;
    }
}