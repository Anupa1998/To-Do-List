package tm;

public class ToDoTM {
    private String todo_ID;
    private String description;
    private String uNtame;

    public ToDoTM() {
    }

    public ToDoTM(String todo_ID, String description, String uNtame) {
        this.todo_ID = todo_ID;
        this.description = description;
        this.uNtame = uNtame;
    }

    public String getTodo_ID() {
        return todo_ID;
    }

    public void setTodo_ID(String todo_ID) {
        this.todo_ID = todo_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getuNtame() {
        return uNtame;
    }

    public void setuNtame(String uNtame) {
        this.uNtame = uNtame;
    }

    public String toString(){
        return description;
    }
}
