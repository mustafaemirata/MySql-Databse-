import java.sql.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        int id = 0;

        Scanner scan=new Scanner(System.in);
        dbHelper db = new dbHelper();
        Connection connection = null;

        try {
            connection = db.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM degerler");
            while (resultSet.next()) {
                id= resultSet.getInt("ogrenci_id");
                String name = resultSet.getString("ogrenci_ad");
                String soyad= resultSet.getNString("ogrenci_soyad");
                System.out.println("ID: " + id + ", Name: " + name+"Öğrenci Soyadı: "+soyad);
                id++;
            }
        } catch (SQLException exception) {
            db.showErrorMessage(exception);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Connection close error: " + e.getMessage());
            }
        }

        System.out.println("Eğer kullanıcı eklemek istiyorsanız 1 'e basınız.");
        int secim=scan.nextInt();

        if(secim==1){
            Scanner scanner = new Scanner(System.in);

            System.out.print("İsim girin: ");
            String firstName = scanner.nextLine();

            System.out.print("Soyisim girin: ");
            String lastName = scanner.nextLine();

            try {

                connection = db.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO degerler (ogrenci_id,ogrenci_ad, ogrenci_soyad) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 1) {

                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        System.out.println("Yeni kullanıcı başarıyla eklendi.");
                    }
                } else {
                    System.out.println("Kullanıcı eklenirken bir hata oluştu.");
                }
            } catch (SQLException exception) {
                db.showErrorMessage(exception);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    System.out.println("Bağlantı kapatma hatası: " + e.getMessage());
                    System.out.println();
                }
            }
        }
    }
}

