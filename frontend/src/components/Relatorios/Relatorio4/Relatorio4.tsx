import React, { useEffect, useState } from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import { getRelatorio4, Relatorio4 } from '../../../api/api'
import './Relatorio4.css'

interface RelatorioAcessosProps {
  ano?: number;
}

export default function RelatorioAcessos({ ano }: RelatorioAcessosProps) {
    const [data, setData] = useState<Relatorio4[]>([]);
    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        async function fetchData() {
            setLoading(true);
            setErrorMessage('');
            try {
                if (ano !== undefined) {
                    const response = await getRelatorio4(ano);
                    setData(response);
                }
            }
            catch(error: any) {
              const message = error instanceof Error ? error.message : 'Erro ao buscar dados do relatório.';
              setErrorMessage(message);
              console.error('Erro ao buscar dados do relatório:', message);
            }
            finally {
              setLoading(false);
            }
        }
        fetchData();
    }, [ano])

  return (
    <div className="relatorio4-container">
      {errorMessage && <p className="relatorio4-error">{errorMessage}</p>}
      
      {!errorMessage && loading && <p className="relatorio4-loading">Carregando dados...</p>}

      {!loading && !errorMessage && data.length === 0 && (
        <p className="relatorio4-empty">Nenhum dado disponível para este ano.</p>
      )}

      {!loading && !errorMessage && data.length > 0 && (
        <div className="relatorio4-chart-wrapper">
          <ResponsiveContainer width="100%" height={400}>
            <BarChart
              data={data}
              margin={{ top: 20, right: 30, left: 0, bottom: 20 }}
            >
              <XAxis 
                dataKey="mes" 
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
                dataKey="total_downloads_mes" 
                name="Downloads" 
                stackId="a" 
                fill="#10b981" 
                radius={[0, 0, 4, 4]}
              />
              <Bar 
                dataKey="total_visualizacao_mes" 
                name="Visualizações" 
                stackId="a" 
                fill="#8b5cf6" 
                radius={[4, 4, 0, 0]} 
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
}