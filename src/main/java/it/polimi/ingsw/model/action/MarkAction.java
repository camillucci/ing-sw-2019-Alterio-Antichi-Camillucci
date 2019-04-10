package it.polimi.ingsw.model.action;

public class MarkAction extends ShootAction
{
    private static final int totPlayer = 5;
    private int[] totMarks;

    protected MarkAction()
    {
        this.shootFuncP = (a,b)-> addMarks();
    }

    public MarkAction(int markP1)
    {
        this();
        totMarks = new int[] {markP1};
    }
    public MarkAction(int markP1, int markP2)
    {
        this();
        totMarks = new int[] {markP1, markP2};
    }
    public MarkAction(int markP1, int markP2, int markP3)
    {
        this();
        totMarks = new int[] {markP1, markP2, markP3};
    }
    public MarkAction(int markP1, int markP2, int markP3, int markP4)
    {
        this();
        totMarks = new int[] {markP1, markP2, markP3, markP4};
    }
    public MarkAction(int markP1, int markP2, int damageP3, int markP4, int markP5)
    {
        this();
        totMarks = new int[] {markP1, markP2, damageP3, markP4, markP5};
    }
    private void addMarks()
    {
        for(int i=0; i < totPlayer; i++)
            if(totMarks[i] > 0)
                this.targetPlayers.get(i).addMark(this.ownerPlayer, totMarks[i]);
    }
}
