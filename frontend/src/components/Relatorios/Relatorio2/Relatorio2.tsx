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
import { getRelatorio2, Relatorio2 } from '../../../api/api';
import './Relatorio2.css';

export default function RelatorioRanking() {
  const [data, setData] = useState<Relatorio2[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      setErrorMessage('');
      try {
        const response = await getRelatorio2();
        setData(response);
      } catch (error: any) {
        const message = error instanceof Error ? error.message : 'Erro ao buscar dados do relatório.';
        setErrorMessage(message);
        console.error('Erro ao buscar dados do relatório:', message);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  return (
    <div className="relatorio2-container">
      {errorMessage && <p className="relatorio2-error">{errorMessage}</p>}

      {!errorMessage && loading && <p className="relatorio2-loading">Carregando dados...</p>}

      {!loading && !errorMessage && data.length === 0 && (
        <p className="relatorio2-empty">Nenhum dado disponível.</p>
      )}

      {!loading && !errorMessage && data.length > 0 && (
        <div className="relatorio2-chart-wrapper">
          <ResponsiveContainer width="100%" height={400}>
            <BarChart
              data={data}
              margin={{ top: 20, right: 30, left: 0, bottom: 20 }}
              layout="vertical"
            >
              <XAxis
                type="number"
                tick={{ fill: '#64748b', fontSize: 12 }}
                axisLine={{ stroke: '#e2e8f0' }}
                tickLine={false}
              />
              <YAxis
                type="category"
                dataKey="nomeDataset"
                tick={{ fill: '#64748b', fontSize: 12 }}
                width={120}
                axisLine={{ stroke: '#e2e8f0' }}
                tickLine={false}
              />

              <Tooltip
                cursor={{ fill: '#f1f5f9', opacity: 0.5 }}
                contentStyle={{
                  backgroundColor: '#ffffff',
                  border: '1px solid #e2e8f0',
                  borderRadius: '10px',
                  color: '#0f172a',
                  padding: '12px'
                }}
                itemStyle={{ color: '#0f172a' }}
                labelStyle={{ color: '#0f172a' }}
              />

              <Legend
                verticalAlign="top"
                align="left"
                iconType="square"
                wrapperStyle={{ paddingBottom: '20px', fontSize: '14px', color: '#475569' }}
              />

              <Bar
                dataKey="totalDownloads"
                name="Downloads"
                fill="#10b981"
                radius={[0, 4, 4, 0]}
              />
              <Bar
                dataKey="totalVisualizacoes"
                name="Visualizações"
                fill="#8b5cf6"
                radius={[0, 4, 4, 0]}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
}