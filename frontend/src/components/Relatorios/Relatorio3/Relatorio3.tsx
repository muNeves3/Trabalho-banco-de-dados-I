import React, { useEffect, useState } from 'react';
import {
  getRelatorio3Contribuintes,
  getRelatorio3Acessos,
  getRelatorio3Downloads,
  Relatorio3Item
} from '../../../api/api';
import './Relatorio3.css';
import { Bar, BarChart, LabelList, ResponsiveContainer, XAxis, YAxis } from 'recharts';

type TipoVisualizacao = 'contribuintes' | 'acessos' | 'downloads';

export default function Relatorio3() {
  const [data, setData] = useState<Relatorio3Item[]>([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [tipoVisualizacao, setTipoVisualizacao] = useState<TipoVisualizacao>('contribuintes');

  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      setErrorMessage('');
      try {
        let response: Relatorio3Item[];
        
        if (tipoVisualizacao === 'contribuintes') {
          response = await getRelatorio3Contribuintes();
        } else if (tipoVisualizacao === 'acessos') {
          response = await getRelatorio3Acessos();
        } else {
          response = await getRelatorio3Downloads();
        }
        
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
  }, [tipoVisualizacao]);

  const getTitulo = (): string => {
    switch (tipoVisualizacao) {
      case 'contribuintes':
        return 'Usuários que Mais Contribuíram';
      case 'acessos':
        return 'Usuários com Mais Visualizações';
      case 'downloads':
        return 'Usuários com Mais Downloads';
    }
  };

  const getLabel = (): string => {
    switch (tipoVisualizacao) {
      case 'contribuintes':
        return 'Contribuições';
      case 'acessos':
        return 'Visualizações';
      case 'downloads':
        return 'Downloads';
    }
  };

  return (
    <div className="relatorio3-container">
      <div className="relatorio3-buttons">
        <button
          type="button"
          className={`relatorio3-button ${tipoVisualizacao === 'contribuintes' ? 'active' : ''}`}
          onClick={() => setTipoVisualizacao('contribuintes')}
        >
          Contribuintes
        </button>
        <button
          type="button"
          className={`relatorio3-button ${tipoVisualizacao === 'acessos' ? 'active' : ''}`}
          onClick={() => setTipoVisualizacao('acessos')}
        >
          Visualizações
        </button>
        <button
          type="button"
          className={`relatorio3-button ${tipoVisualizacao === 'downloads' ? 'active' : ''}`}
          onClick={() => setTipoVisualizacao('downloads')}
        >
          Downloads
        </button>
      </div>

      {errorMessage && <p className="relatorio3-error">{errorMessage}</p>}

      {!errorMessage && loading && <p className="relatorio3-loading">Carregando dados...</p>}

      {!loading && !errorMessage && data.length === 0 && (
        <p className="relatorio3-empty">Nenhum dado disponível.</p>
      )}

      {!loading && !errorMessage && data.length > 0 && (
        <div style={{ width: '100%', height: 600, alignContent: 'center', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ backgroundColor: '#121418', padding: '40px 20px', borderRadius: '8px', width: '95%' }}>
              <ResponsiveContainer width="100%" height={500}>
                <BarChart
                  data={data}
                  layout="vertical"
                  margin={{ top: 20, right: 130, left: 20, bottom: 10 }} 
                >
                  <XAxis
                    type="number"
                    stroke="#ffffff"
                    tick={{ fill: '#ffffff', fontSize: 14 }}
                    axisLine={{ stroke: '#ffffff' }}
                    tickCount={15} 
                  />
                
                  <YAxis
                    type="category"
                    dataKey="nome"
                    stroke="#ffffff"
                    tick={{ fill: '#ffffff', fontSize: 14 }}
                    axisLine={{ stroke: '#ffffff' }}
                    tickLine={false} 
                    width={90} 
                  />
                  <Bar dataKey="count" fill="#f4b251" barSize={50}>
                    <LabelList
                      dataKey="count"
                      position="right"
                      fill="#ffffff"
                      formatter={(value) => `${value} visualizações`}
                      style={{ fontSize: '14px', fontWeight: 500 }}
                    />
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </div>
        </div>
      )}
    </div>
  );
}
