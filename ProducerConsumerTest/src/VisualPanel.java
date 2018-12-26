import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

class VisualPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private int ConsumerAmount[];
	private int ProducerAmount[];
	private int BufferSize;
	private Integer bufferContent[];
	
	VisualPanel()
	{
		ConsumerAmount = new int [8];
		ProducerAmount = new int [8];
	}
	
	public void ChangeBufferSize (int size)
	{
		BufferSize = size;
	}
	public void prepareSituation(Integer c[], int d, int e, boolean f, int g, int h)
    {
        bufferContent = c;
        cquan = d;
        pquan = e;
        cacti = f;
        cpid = g - 1;
        if(cacti)
        	ConsumerAmount[cpid] = h;
        else
        	ProducerAmount[cpid] = h;
    }

    public void drawSituation(Graphics g)
    {
        int box = 36;
        Graphics2D g2 = (Graphics2D)g;
        Stroke t = g2.getStroke();
        g2.setColor(Color.GRAY);
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2.setStroke(new BasicStroke(2.0F));
        g2.setColor(Color.MAGENTA);
        if(BufferSize > 0)
        {
            if(bufferContent.length == BufferSize)
                g2.fillRect(getWidth() / 2 - (BufferSize * box) / 2, getHeight() / 2 - box / 2, BufferSize * box, box);
            else
                g2.drawRect(getWidth() / 2 - (BufferSize * box) / 2, getHeight() / 2 - box / 2, BufferSize * box, box);
            g2.setColor(Color.BLACK);
            String s = "";
            for(int i = BufferSize - 1; i >= 0; i--)
                if(i < bufferContent.length)
                    s = (new StringBuilder()).append(s).append("|").append((char)bufferContent[i].intValue()).append("|").toString();
                else
                    s = (new StringBuilder()).append(s).append("| |").toString();

            g2.setFont(new Font("Monospaced", 1, 20));
            g2.drawString(s, getWidth() / 2 - (BufferSize * box) / 2, getHeight() / 2 + 5);
        }
        for(int i = 0; i < pquan; i++)
        {
            int w;
            if(ProducerAmount[i] == 2)
                w = 130;
            else
            if(ProducerAmount[i] == 1)
                w = 80;
            else
                w = 0;
            g2.setColor(Color.GREEN);
            if(!cacti && cpid == i)
                g2.setColor(Color.RED);
            g2.fillOval(w, (getHeight() / 2 - (pquan * box) / 2) + i * box, box, box);
        }

        for(int i = 0; i < cquan; i++)
        {
            int w;
            if(ConsumerAmount[i] == 2)
                w = getWidth() - 130 - box;
            else
            if(ConsumerAmount[i] == 1)
                w = getWidth() - 80 - box;
            else
                w = getWidth() - box;
            g2.setColor(Color.BLUE);
            if(cacti && cpid == i)
                g2.setColor(Color.RED);
            g2.fillOval(w, (getHeight() / 2 - (cquan * box) / 2) + i * box, box, box);
        }

        g2.setStroke(t);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawSituation(g);
    }

    private int cquan;
    private int pquan;
    private boolean cacti;
    private int cpid;

	
}