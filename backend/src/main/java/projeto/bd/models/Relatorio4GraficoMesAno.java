package projeto.bd.models;

public class Relatorio4GraficoMesAno {
	private int total_downloads_mes;
	private int total_visualizacao_mes;
	private String mes;

	public Relatorio4GraficoMesAno(int total_downloads_mes, int total_visualizacao_mes, String mes) {
		this.total_downloads_mes = total_downloads_mes;
		this.total_visualizacao_mes = total_visualizacao_mes;
		this.mes = mes;
	}

	public int getTotal_downloads_mes() {
		return total_downloads_mes;
	}
	public void setTotal_downloads_mes(int total_downloads_mes) {
		this.total_downloads_mes = total_downloads_mes;
	}
	public int getTotal_visualizacao_mes() {
		return total_visualizacao_mes;
	}
	public void setTotal_visualizacao_mes(int total_visualizacao_mes) {
		this.total_visualizacao_mes = total_visualizacao_mes;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
}

