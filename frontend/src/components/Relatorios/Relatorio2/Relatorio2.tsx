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

type TipoVisualizacao = 'proporcao' | 'acessos' | 'downloads';

export default function RelatorioRanking() {
  const [data, setData] = useState<Relatorio2[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [tipoVisualizacao, setTipoVisualizacao] = useState<TipoVisualizacao>('proporcao');
  

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

  const getTitulo = (): string => {
    switch (tipoVisualizacao) {
      case 'proporcao':
        return 'Proporção visualização / download por dataset';
      case 'acessos':
        return 'Datasets com Mais Visualizações';
      case 'downloads':
        return 'Datasets com Mais Downloads';
    }
  };

  const getLabel = (): string => {
    switch (tipoVisualizacao) {
      case 'proporcao':
        return 'Proporção';
      case 'acessos':
        return 'Visualizações';
      case 'downloads':
        return 'Downloads';
    }
  };

  const getDadosOrdenados = (): Relatorio2[] => {
    switch (tipoVisualizacao) {
      case 'acessos':
        return [...data].sort((a, b) => b.totalVisualizacoes - a.totalVisualizacoes);
      case 'downloads':
        return [...data].sort((a, b) => b.totalDownloads - a.totalDownloads);
      default:
        return [...data].sort((a, b) => b.totalAcessos - a.totalAcessos);
    }
  };

  return (
    <div className="relatorio2-container">
      <div className="relatorio2-buttons">
        <button
          type="button"
          className={`relatorio2-button ${tipoVisualizacao === 'proporcao' ? 'active' : ''}`}
          onClick={() => setTipoVisualizacao('proporcao')}
        >
          Proporção
        </button>
        <button
          type="button"
          className={`relatorio2-button ${tipoVisualizacao === 'acessos' ? 'active' : ''}`}
          onClick={() => setTipoVisualizacao('acessos')}
        >
          Visualizações
        </button>
        <button
          type="button"
          className={`relatorio2-button ${tipoVisualizacao === 'downloads' ? 'active' : ''}`}
          onClick={() => setTipoVisualizacao('downloads')}
        >
          Downloads
        </button>
      </div>
      <h4>{getTitulo()}</h4>
      {errorMessage && <p className="relatorio2-error">{errorMessage}</p>}

      {!errorMessage && loading && <p className="relatorio2-loading">Carregando dados...</p>}

      {!loading && !errorMessage && data.length === 0 && (
        <p className="relatorio2-empty">Nenhum dado disponível.</p>
      )}

      {!loading && !errorMessage && data.length > 0 && (
        <div className="relatorio2-chart-wrapper">
          <ResponsiveContainer width="100%" height={400}>
            <BarChart
              data={getDadosOrdenados()}
              margin={{ top: 20, right: 30, left: 0, bottom: 20 }}
            >
              <XAxis
                type="category"
                dataKey="nomeDataset"
                tick={{ fill: '#64748b', fontSize: 12 }}
                axisLine={{ stroke: '#e2e8f0' }}
                tickLine={false}
              />
              <YAxis
                type="number"
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

              {tipoVisualizacao !== 'acessos' && (
                <Bar
                  dataKey="totalDownloads"
                  name="Downloads"
                  fill="#10b981"
                  radius={[4, 4, 0, 0]}
                />
              )}
              {tipoVisualizacao !== 'downloads' && (
                <Bar
                  dataKey="totalVisualizacoes"
                  name="Visualizações"
                  fill="#8b5cf6"
                  radius={[4, 4, 0, 0]}
                />
              )}
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
}