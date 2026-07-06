import React, { useEffect, useState } from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import { getRelatorio6, Relatorio6 } from '../../../api/api';
import './Relatorio6.css';

export default function RelatorioHorarios() {
  const [data, setData] = useState<Relatorio6[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      setErrorMessage('');
      try {
        const response = await getRelatorio6();
        const formatado = response.map((item) => ({
          ...item,
          horaLabel: `${item.hora}h`
        }));
        setData(formatado);
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
    <div className="relatorio6-container">
      {errorMessage && <p className="relatorio6-error">{errorMessage}</p>}
      {!errorMessage && loading && <p className="relatorio6-loading">Carregando dados...</p>}
      {!loading && !errorMessage && data.length === 0 && (
        <p className="relatorio6-empty">Nenhum dado disponível.</p>
      )}
      {!loading && !errorMessage && data.length > 0 && (
        <div className="relatorio6-chart-wrapper">
          <ResponsiveContainer width="100%" height={400}>
            <BarChart
              data={data}
              margin={{ top: 20, right: 30, left: 0, bottom: 20 }}
            >
              <XAxis
                dataKey="horaLabel"
                tick={{ fill: '#64748b', fontSize: 12 }}
                axisLine={{ stroke: '#e2e8f0' }}
                tickLine={false}
              />
              <YAxis
                tick={{ fill: '#64748b', fontSize: 12 }}
                axisLine={{ stroke: '#e2e8f0' }}
                tickLine={false}
              />
              <Tooltip
                cursor={{ fill: '#f1f5f9', opacity: 0.5 }}
                contentStyle={{
                  backgroundColor: '#ffffff',
                  border: '1px solid #e2e8f0',
                  borderRadius: '10px',
                  padding: '12px'
                }}
              />
              <Legend
                verticalAlign="top"
                align="left"
                iconType="square"
                wrapperStyle={{ paddingBottom: '20px', fontSize: '14px' }}
              />
              <Bar
                dataKey="totalVisualizacoes"
                name="Visualizações"
                fill="#8b5cf6"
                radius={[4, 4, 0, 0]}
              />
              <Bar
                dataKey="totalDownloads"
                name="Downloads"
                fill="#10b981"
                radius={[4, 4, 0, 0]}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
}