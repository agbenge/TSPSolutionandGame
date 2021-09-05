package softcare.game.model;

public class ProgressX {
    private String title;
    private int progress;
    private int size;
    private  double progressPercent;
    private  double progressProbability;

    public ProgressX(String title) {
        this.title = title;
    }

    public ProgressX(String title, int progress, int size, double progressPercent, double progressProbability) {
        this.title = title;
        this.progress = progress;
        this.size = size;
        this.progressPercent = progressPercent;
        this.progressProbability = progressProbability;
    }

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

    public int getSize() {
        return size;
    }

    public double getProgressPercent() {
        return progressPercent;
    }

    public double getProgressProbability() {
        return progressProbability;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }

    public void setProgressProbability(double progressProbability) {
        this.progressProbability = progressProbability;
    }

    @Override
    public String toString() {
        return "ProgressX{" +
                "title='" + title + '\'' +
                ", progress=" + progress +
                ", size=" + size +
                ", progressPercent=" + progressPercent +
                ", progressProbability=" + progressProbability +
                '}';
    }
}
