package cn.edu.zucc.booklib.control;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.booklib.model.BeanSystemUser;
import cn.edu.zucc.booklib.util.BaseException;
import cn.edu.zucc.booklib.util.BusinessException;
import cn.edu.zucc.booklib.util.DBUtil;
import cn.edu.zucc.booklib.util.DbException;

public class SystemUserManager {
	public static BeanSystemUser currentUser=null;
	public BeanSystemUser reg(String userid, String pwd1, String pwd2,String username, String uemail,String uphoto) throws BaseException
	{
		Connection conn = null;
		BeanSystemUser bsu= new BeanSystemUser();
		try {
			//exception���
			if("".equals(userid))
				throw new BaseException("�û��˺Ų���Ϊ��");
			if(!pwd1.equals(pwd2))
				throw new BaseException("����������������һ��");
			if("".equals(pwd1)||"".equals(pwd2))
				throw new BaseException("���벻��Ϊ��");
			if(userid.length()>45||pwd1.length()>45||pwd2.length()>45)
				throw new BaseException("�û��ǳƻ����볤�Ȳ��ܳ���45���ַ�");
			if("".equals(username))
				throw new BaseException("������������Ϊ��");
			
			conn = DBUtil.getConnection();
			String sql = "select * from BeanSystemUser where userid = ?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			java.sql.ResultSet rs = pst.executeQuery();
			if(rs.next())
				throw new BaseException("�û��˻��Ѿ����ڣ��뻻һ����");
			rs.close();
//			//��Ѱ���cid�Ӷ��������û�cid�����ñ���Ϊusercid��
//			
//			sql = "select max(cid) from BeanCus ";
//			pst = conn.prepareStatement(sql);
//			java.sql.ResultSet rs1 = pst.executeQuery();
//			if(rs1.next())
//				usercid = rs1.getInt(1)+1;//�ҵ�cid�õ��͸�ֵ+1
//			else 
//				usercid = 1;
//			rs1.close();
			
			//ע�ᣬ���ݴ������ݿ�
			sql = "insert into BeanSystemUser(userid,username,pwd,usertype,createdate,removedate,uemail,uphoto) values(?,?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			pst.setString(2, username);
			pst.setString(3, pwd1);
			pst.setString(4, "����Ա");
			pst.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
			pst.setString(6, null);
			pst.setString(7, uemail);
			pst.setString(8, uphoto);
			pst.execute();
			
			//Return BeanSystemUser
			sql = "select * from BeanSystemUser where userid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			java.sql.ResultSet rs2 = pst.executeQuery();
			rs2.next();
			bsu.setUserid(userid);
			bsu.setUsername(username);
			bsu.setPwd(pwd1);
			bsu.setUsertype("����Ա");
			bsu.setCreateDate(rs2.getTimestamp(5));
			bsu.setUemail(uemail);
			bsu.setUphoto(uphoto);
			
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally {
			try {
				if(conn!=null)
					conn.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
			
		}
		return bsu;
	}
	public void deleteUserInHard(String userid) throws BaseException
	{
		Connection conn = null;
		try {
			String sql = "select * from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			java.sql.ResultSet rs = pst.executeQuery();
			if(!rs.next())
				throw new BusinessException("None");
			rs.close();
			pst.close();
			sql = "delete from BeanSystemUser where userid=?";
			pst.setString(1, userid);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	public boolean modifyUserName(String userid, String username) throws BaseException
	{
		Connection conn = null;
		try {
			String sql = "select * from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			java.sql.ResultSet rs = pst.executeQuery();
			if(!rs.next())
				return false;
			rs.close();
			pst.close();
			sql = "update BeanSystemUser set username = ? where userid = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setString(2, userid);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return true;
	}
	public List<BeanSystemUser> loadAllUsers(boolean withDeletedUser)throws BaseException{
		List<BeanSystemUser> result=new ArrayList<BeanSystemUser>();
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select userid,username,usertype,createDate from BeanSystemUser";
			if(!withDeletedUser)
				sql+=" where removeDate is null ";
			sql+=" order by userid";
			java.sql.Statement st=conn.createStatement();
			java.sql.ResultSet rs=st.executeQuery(sql);
			while(rs.next()){
				BeanSystemUser u=new BeanSystemUser();
				u.setUserid(rs.getString(1));
				u.setUsername(rs.getString(2));
				u.setUsertype(rs.getString(3));
				u.setCreateDate(rs.getDate(4));
				result.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return result;
	}
	public void createUser(BeanSystemUser user)throws BaseException{
		if(user.getUserid()==null || "".equals(user.getUserid()) || user.getUserid().length()>20){
			throw new BusinessException("��½�˺ű�����1-20����");
		}
		if(user.getUsername()==null || "".equals(user.getUsername()) || user.getUsername().length()>50){
			throw new BusinessException("�˺����Ʊ�����1-50����");
		}
		if(!"����Ա".equals(user.getUsertype()) && !"����Ա".equals(user.getUsertype())){
			throw new BusinessException("�û��������ǽ���Ա�����Ա");
		}
		
		
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select * from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,user.getUserid());
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next()) throw new BusinessException("��½�˺��Ѿ�����!!");
			rs.close();
			pst.close();
			sql="insert into BeanSystemUser(userid,username,pwd,usertype,createDate) values(?,?,?,?,?)";
			pst=conn.prepareStatement(sql);
			pst.setString(1, user.getUserid());
			pst.setString(2, user.getUsername());
			user.setPwd(user.getUserid());//Ĭ������Ϊ�˺�
			pst.setString(3,user.getPwd());
			pst.setString(4, user.getUsertype());
			pst.setTimestamp(5,new java.sql.Timestamp(System.currentTimeMillis()));
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void changeUserPwd(String userid,String oldPwd,String newPwd)throws BaseException{
		if(oldPwd==null) throw new BusinessException("ԭʼ���벻��Ϊ��");
		if(newPwd==null || "".equals(newPwd) || newPwd.length()>16) throw new BusinessException("����Ϊ1-16���ַ�");
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select pwd from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,userid);
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("��½�˺Ų� ����");
			if(!oldPwd.equals(rs.getString(1))) throw new BusinessException("ԭʼ�������");
			rs.close();
			pst.close();
			sql="update BeanSystemUser set pwd=? where userid=?";
			pst=conn.prepareStatement(sql);
			pst.setString(1, newPwd);
			pst.setString(2, userid);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void resetUserPwd(String userid)throws BaseException{
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select * from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,userid);
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("��½�˺Ų� ����");
			rs.close();
			pst.close();
			sql="update BeanSystemUser set pwd=? where userid=?";
			pst=conn.prepareStatement(sql);
			pst.setString(1, userid);
			pst.setString(2, userid);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void deleteUser(String userid)throws BaseException{
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select removeDate from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,userid);
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("��½�˺Ų� ����");
			if(rs.getDate(1)!=null) throw new BusinessException("���˺��Ѿ���ɾ��");
			rs.close();
			pst.close();
			sql="update BeanSystemUser set removeDate=? where userid=?";
			pst=conn.prepareStatement(sql);
			pst.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
			pst.setString(2, userid);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public BeanSystemUser loadUser(String userid)throws BaseException{
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select userid,username,pwd,usertype,createDate,removeDate from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,userid);
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("��½�˺Ų�����");
			BeanSystemUser u=new BeanSystemUser();
			u.setUserid(rs.getString(1));
			u.setUsername(rs.getString(2));
			u.setPwd(rs.getString(3));
			u.setUsertype(rs.getString(4));
			u.setCreateDate(rs.getDate(5));
			u.setRemoveDate(rs.getDate(6));
			if(u.getRemoveDate()!=null) throw new BusinessException("���˺��Ѿ���ɾ��");
			rs.close();
			pst.close();
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	public static void main(String[] args){
		BeanSystemUser user=new BeanSystemUser();
		user.setUserid("admin");
		user.setUsername("ϵͳ����Ա");
		user.setUsertype("����Ա");
		try {
			new SystemUserManager().createUser(user);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
