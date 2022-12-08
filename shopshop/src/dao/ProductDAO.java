package dao;

import java.util.List;

import bean.Product;

public interface ProductDAO {

	public void addProduct(Product p);

	public List<Product> getList();

	public List<Product> getListByCategory(int ma_the_loai);
	
	public Product getProduct(int ma_san_pham);
	
	public List<Product> searchList(String ten_san_pham, String ten_the_loai);

}
