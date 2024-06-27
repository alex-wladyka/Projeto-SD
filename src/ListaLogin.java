import Utils.JsonUtils;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListaLogin extends JFrame {
    private JPanel panel1;
    private JList<UserConnected> loginList;
    private static DefaultListModel<UserConnected> loginListModel = new DefaultListModel();
    private static List<UserConnected> usuariosConnectados = new ArrayList<>();

    public ListaLogin() {
        setTitle("Lista de Usuários Logados");
        setSize(1000, 500);
        setContentPane(panel1);
        setVisible(true);
        loginList.setModel(loginListModel);


    }

    public static void updateLista(List<String> usuarios, List<String> ips) throws SQLException {
        PreparedStatement st;
        ResultSet rs;
        usuariosConnectados.clear();

        for (int i = 0; i < usuarios.size(); i++){
            System.out.println("teste");
            String email="";
            String ip = ips.get(i);
            String id = String.valueOf(JsonUtils.JWTValidator.getIdClaim(usuarios.get(i)));
            String role = JsonUtils.JWTValidator.getRoleClaim(usuarios.get(i));


            if(role.equals("CANDIDATE")){
                st = Conexao.getConexao().prepareStatement("SELECT Email FROM candidate WHERE id = ?");
                st.setString(1, id);

                rs = st.executeQuery();
                rs.next();

                email = rs.getString("Email");

            }
            else if(role.equals("RECRUITER")){
                st = Conexao.getConexao().prepareStatement("SELECT email FROM recruiter WHERE id = ?");
                st.setString(1, id);

                rs = st.executeQuery();
                rs.next();

                email = rs.getString("email");
            }

            usuariosConnectados.add(new UserConnected(ip, id, role, email));
        }

        loginListModel.removeAllElements();
        if(!usuarios.isEmpty()){
            loginListModel.addAll(usuariosConnectados);
        }
    }

    public static void removerLista(String ip) throws SQLException  {
        usuariosConnectados.remove(ip);
        System.out.println(usuariosConnectados);
    }


}
