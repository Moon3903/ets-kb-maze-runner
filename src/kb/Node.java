package kb;

public class Node {
    private int y;
    private int x;
    private int parentX;
    private int parentY;
    private int gCost;
    private int hCost;
    private int fCost;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;

        this.fCost = Integer.MAX_VALUE;
        this.gCost = Integer.MAX_VALUE;
        this.hCost = Integer.MAX_VALUE;

        this.parentX = -1;
        this.parentY = -1;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getParentX() {
        return parentX;
    }
    public void setParentX(int parentX) {
        this.parentX = parentX;
    }

    public int getParentY() {
        return parentY;
    }
    public void setParentY(int parentY) {
        this.parentY = parentY;
    }

    public int getGCost() {
        return gCost;
    }
    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public float getHCost() {
        return hCost;
    }
    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return fCost;
    }
    public void setFCost(int fCost) {
        this.fCost = fCost;
    }
}
