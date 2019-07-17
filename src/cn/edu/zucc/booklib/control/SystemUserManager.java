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
			//exception检测
			if("".equals(userid))
				throw new BaseException("用户账号不能为空");
			if(!pwd1.equals(pwd2))
				throw new BaseException("两次输入的密码必须一致");
			if("".equals(pwd1)||"".equals(pwd2))
				throw new BaseException("密码不能为空");
			if(userid.length()>45||pwd1.length()>45||pwd2.length()>45)
				throw new BaseException("用户昵称或密码长度不能超过45个字符");
			if("".equals(username))
				throw new BaseException("您的姓名不能为空");
			
			conn = DBUtil.getConnection();
			String sql = "select * from BeanSystemUser where userid = ?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			java.sql.ResultSet rs = pst.executeQuery();
			if(rs.next())
				throw new BaseException("用户账户已经存在，请换一个！");
			rs.close();
//			//搜寻最大cid从而生成新用户cid，设置变量为usercid；
//			
//			sql = "select max(cid) from BeanCus ";
//			pst = conn.prepareStatement(sql);
//			java.sql.ResultSet rs1 = pst.executeQuery();
//			if(rs1.next())
//				usercid = rs1.getInt(1)+1;//找得cid得到就赋值+1
//			else 
//				usercid = 1;
//			rs1.close();
			
			//注册，数据存入数据库
			sql = "insert into BeanSystemUser(userid,username,pwd,usertype,createdate,removedate,uemail,uphoto) values(?,?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			pst.setString(2, username);
			pst.setString(3, pwd1);
			pst.setString(4, "借阅员");
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
			bsu.setUsertype("借阅员");
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
			throw new BusinessException("登陆账号必须是1-20个字");
		}
		if(user.getUsername()==null || "".equals(user.getUsername()) || user.getUsername().length()>50){
			throw new BusinessException("账号名称必须是1-50个字");
		}
		if(!"管理员".equals(user.getUsertype()) && !"借阅员".equals(user.getUsertype())){
			throw new BusinessException("用户类别必须是借阅员或管理员");
		}
		
		
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select * from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,user.getUserid());
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next()) throw new BusinessException("登陆账号已经存在!!");
			rs.close();
			pst.close();
			sql="insert into BeanSystemUser(userid,username,pwd,usertype,createDate) values(?,?,?,?,?)";
			pst=conn.prepareStatement(sql);
			pst.setString(1, user.getUserid());
			pst.setString(2, user.getUsername());
			user.setPwd(user.getUserid());//默认密码为账号
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
		if(oldPwd==null) throw new BusinessException("原始密码不能为空");
		if(newPwd==null || "".equals(newPwd) || newPwd.length()>16) throw new BusinessException("必须为1-16个字符");
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select pwd from BeanSystemUser where userid=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,userid);
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("登陆账号不 存在");
			if(!oldPwd.equals(rs.getString(1))) throw new BusinessException("原始密码错误");
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
			if(!rs.next()) throw new BusinessException("登陆账号不 存在");
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
			if(!rs.next()) throw new BusinessException("登陆账号不 存在");
			if(rs.getDate(1)!=null) throw new BusinessException("该账号已经被删除");
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
			if(!rs.next()) throw new BusinessException("登陆账号不存在");
			BeanSystemUser u=new BeanSystemUser();
			u.setUserid(rs.getString(1));
			u.setUsername(rs.getString(2));
			u.setPwd(rs.getString(3));
			u.setUsertype(rs.getString(4));
			u.setCreateDate(rs.getDate(5));
			u.setRemoveDate(rs.getDate(6));
			if(u.getRemoveDate()!=null) throw new BusinessException("该账号已经被删除");
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
		user.setUsername("系统管理员");
		user.setUsertype("管理员");
		try {
			new SystemUserManager().createUser(user);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
