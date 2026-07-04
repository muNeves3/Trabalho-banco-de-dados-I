import React, { useEffect, useState } from 'react';
import { getRelatorio5, Relatorio5 } from '../../../api/api';
import './Relatorio5.css';

export default function RelatorioVersoes() {
  const [data, setData] = useState<Relatorio5[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      setErrorMessage('');
      try {
        const response = await getRelatorio5();
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
      {!loading && !errorMessage && data.length === 0 && (
        <p className="relatorio5-empty">Nenhum dado disponível.</p>
      )}
      {!loading && !errorMessage && data.length > 0 && (
        <table className="relatorio5-table">
          <thead>
            <tr>
              <th>Dataset</th>
              <th>Total de Versões</th>
              <th>Tempo Médio entre Versões</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item) => (
              <tr key={item.datasetId}>
                <td>{item.nomeDataset}</td>
                <td>{item.totalVersoes}</td>
                <td>{item.tempoMedio ? item.tempoMedio.split(' ')[0] + ' dias' : '—'}</td>              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}