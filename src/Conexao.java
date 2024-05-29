import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String url = "jdbc:mysql://localhost:3306/projeto_sd";
    private static final String user = "root";
    private static final String password = "Distribuidos";


    private static Connection con;



    public static Connection getConexao(){
        try{
            if (con == null){
                //Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url,user,password);
                return con;
            } else{
                return con;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }




}