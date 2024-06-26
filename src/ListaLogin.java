import Utils.JsonUtils;

import javax.swing.*;
import java.awt.*;
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

    public static void updateLista(List<String> usuarios)
    {
        usuariosConnectados.clear();

        for (int i = 0; i < usuarios.size(); i++){
            System.out.println("teste");
            String id = String.valueOf(JsonUtils.JWTValidator.getIdClaim(usuarios.get(i)));
            String role = JsonUtils.JWTValidator.getRoleClaim(usuarios.get(i));
            String token = usuarios.get(i);
            usuariosConnectados.add(new UserConnected(token, id, role));
        }

        loginListModel.removeAllElements();
        if(!usuarios.isEmpty()){
            loginListModel.addAll(usuariosConnectados);
        }
    }


}
