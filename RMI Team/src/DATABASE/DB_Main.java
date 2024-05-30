
package DATABASE;

import OO.TaskDAO;

import java.sql.ResultSet;


public class DB_Main {
    public static void main(String[] args){
        String url = "jdbc:mysql://127.0.0.1:9888/db_tasks";
        String userName = "root";
        String password = "";

        TaskDAO dao = new TaskDAO(url,userName,password);
    }
    }


