//package edu.nutech.tht.repo.impl;
//
//import edu.nutech.tht.model.UserBalance;
//import edu.nutech.tht.repo.UserBalanceRepo;
//import edu.nutech.tht.util.DatabaseUtilities;
//import org.springframework.stereotype.Repository;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Repository
//public class UserBalanceImpl implements UserBalanceRepo {
//    @Override
//    public UserBalance saveUserBalance(UserBalance userBalance) throws SQLException {
////        boolean output = false;
//        UserBalance result = null;
//        if (userBalance != null){
//            result = userBalance;
//
//            Connection con = DatabaseUtilities.getConnection();
//            try {
//                PreparedStatement stat = con.prepareStatement("INSERT INTO user_balance values (?)");
////            stat.setInt(1, userBalance.getIdKendaraan());
////                stat.setString(1, 1);
//                stat.setLong(1, result.getBalance());
//                stat.executeUpdate();
////            output = true;
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            } finally {
//                if (con != null) {
//                    con.close();
//                }
//            }
//        }
//
//        return result;
//    }
//}
