import React, { useEffect, useState } from 'react';
import { getRelatorio1, Relatorio1, Relatorio5 } from '../../../api/api';
import './Relatorio1.css';

export default function RelatorioGeralSistema() {
  const [data, setData] = useState<Relatorio1>({} as Relatorio1);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      setErrorMessage('');
      try {
        const response = await getRelatorio1();
        setData(response);
      } catch (error: any) {
        const message = error instanceof Error ? error.message : 'Erro ao buscar dados.';
        setErrorMessage(message);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  return (
    <div className="relatorio5-container">
      {errorMessage && <p className="relatorio5-error">{errorMessage}</p>}
      {!errorMessage && loading && <p className="relatorio5-loading">Carregando dados...</p>}
      {!loading && !errorMessage && data !== {} as Relatorio1 && (
        <p className="relatorio5-empty">Nenhum dado disponível.</p>
      )}
      {!loading && !errorMessage && data !== {} as Relatorio1 && (
        <table className="relatorio5-table">
          <thead>
            <tr>
              <th>Total de datasets</th>
              <th>Total de versões</th>
              <th>Total de usuários cadastrados</th>
              <th>Média de versões por dataset</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{data.totalDatasets}</td>
              <td>{data.totalVersoes}</td>
              <td>{data.usuariosCadastrados}</td>
              <td>{data.mediaVersoesPorDataset}</td>
            </tr>
          </tbody>
        </table>
      )}
    </div>
  );
}