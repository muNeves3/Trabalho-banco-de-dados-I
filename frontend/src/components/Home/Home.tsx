import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  baixarVersaoDataset,
  cadastrarNovaVersaoDataset,
  Dataset,
  listarDatasets,
  listarVersoesDataset,
  registrarAcesso,
  VersaoDataset,
  visualizarVersaoDataset
} from '../../api/api';
import './Home.css';

type UserSession = {
  cpf: string;
  email: string;
};

type HomeProps = {
  user: UserSession;
  onLogout: () => void;
};

function Home({ user, onLogout }: HomeProps) {
  const navigate = useNavigate();
  const [datasets, setDatasets] = useState<Dataset[]>([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState('');
  const [menuAberto, setMenuAberto] = useState(false);
  const [datasetExpandidoId, setDatasetExpandidoId] = useState<number | null>(null);
  const [versoesPorDataset, setVersoesPorDataset] = useState<Record<number, VersaoDataset[]>>({});
  const [loadingVersoes, setLoadingVersoes] = useState<Record<number, boolean>>({});
  const [versaoSelecionadaParaCriacao, setVersaoSelecionadaParaCriacao] = useState<{ datasetId: number; numeroVersao: number } | null>(null);
  const [descricaoNovaVersao, setDescricaoNovaVersao] = useState('');
  const [arquivoNovaVersaoBase64, setArquivoNovaVersaoBase64] = useState('');
  const [loadingCriacaoVersao, setLoadingCriacaoVersao] = useState(false);
  const [visualizacaoVersao, setVisualizacaoVersao] = useState<VersaoDataset | null>(null);

  useEffect(() => {
    const carregarDatasets = async () => {
      setLoading(true);
      setErrorMessage('');

      try {
        const response = await listarDatasets();
        setDatasets(response);
      } catch (error) {
        const message = error instanceof Error ? error.message : 'Erro ao carregar datasets.';
        setErrorMessage(message);
      } finally {
        setLoading(false);
      }
    };

    carregarDatasets();
  }, []);

  const formatter = useMemo(
    () =>
      new Intl.DateTimeFormat('pt-BR', {
        dateStyle: 'short',
        timeStyle: 'short'
      }),
    []
  );

  const handleLogout = () => {
    setMenuAberto(false);
    onLogout();
    navigate('/login');
  };

  const handleExpandirDataset = async (datasetId: number) => {
    if (datasetExpandidoId === datasetId) {
      setDatasetExpandidoId(null);
      setVersaoSelecionadaParaCriacao(null);
      setVisualizacaoVersao(null);
      return;
    }

    setDatasetExpandidoId(datasetId);
    setVersaoSelecionadaParaCriacao(null);
    setVisualizacaoVersao(null);

    if (versoesPorDataset[datasetId]) {
      return;
    }

    try {
      setLoadingVersoes((prev) => ({ ...prev, [datasetId]: true }));
      const versoes = await listarVersoesDataset(datasetId);
      setVersoesPorDataset((prev) => ({ ...prev, [datasetId]: versoes }));
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao carregar versões do dataset.';
      setErrorMessage(message);
    } finally {
      setLoadingVersoes((prev) => ({ ...prev, [datasetId]: false }));
    }
  };

  const salvarBlob = (blob: Blob, nomeArquivo: string) => {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = nomeArquivo;
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
  };

  const handleBaixarVersao = async (dataset: Dataset, numeroVersao: number) => {
    try {
      const blob = await baixarVersaoDataset(dataset.id, numeroVersao);
      await registrarAcesso({
        usuarioCpf: user.cpf,
        datasetId: dataset.id,
        numeroVersao,
        tipoAcesso: 'download'
      });
      salvarBlob(blob, `${dataset.nome || `dataset-${dataset.id}`}-v${numeroVersao}.csv`);
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao baixar versão.';
      setErrorMessage(message);
    }
  };

  const handleVisualizarVersao = async (datasetId: number, numeroVersao: number) => {
    try {
      const versao = await visualizarVersaoDataset(datasetId, numeroVersao);
      await registrarAcesso({
        usuarioCpf: user.cpf,
        datasetId,
        numeroVersao,
        tipoAcesso: 'visualizacao'
      });
      setVisualizacaoVersao(versao);
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao visualizar versão.';
      setErrorMessage(message);
    }
  };

  const handleSelecionarCriacaoDeVersao = (datasetId: number, numeroVersao: number) => {
    setVersaoSelecionadaParaCriacao({ datasetId, numeroVersao });
    setDescricaoNovaVersao('');
    setArquivoNovaVersaoBase64('');
  };

  const handleArquivoNovaVersao = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const arquivo = event.target.files?.[0];
    if (!arquivo) {
      setArquivoNovaVersaoBase64('');
      return;
    }

    const conteudoBase64 = await new Promise<string>((resolve, reject) => {
      const leitor = new FileReader();
      leitor.onload = () => {
        const resultado = leitor.result;
        if (typeof resultado === 'string') {
          const partes = resultado.split(',');
          resolve(partes.length > 1 ? partes[1] : partes[0]);
          return;
        }
        reject(new Error('Não foi possível processar o arquivo.'));
      };
      leitor.onerror = () => reject(new Error('Erro ao ler arquivo da nova versão.'));
      leitor.readAsDataURL(arquivo);
    });

    setArquivoNovaVersaoBase64(conteudoBase64);
  };

  const handleCriarNovaVersao = async (datasetId: number) => {
    if (!versaoSelecionadaParaCriacao || versaoSelecionadaParaCriacao.datasetId !== datasetId) {
      return;
    }

    if (!descricaoNovaVersao.trim()) {
      setErrorMessage('Informe a descrição das modificações da nova versão.');
      return;
    }

    if (!arquivoNovaVersaoBase64) {
      setErrorMessage('Selecione o arquivo da nova versão.');
      return;
    }

    setLoadingCriacaoVersao(true);
    setErrorMessage('');

    try {
      await cadastrarNovaVersaoDataset({
        datasetId,
        versaoBaseNumero: versaoSelecionadaParaCriacao.numeroVersao,
        criadorCpf: user.cpf,
        descModificacoes: descricaoNovaVersao,
        arquivo: arquivoNovaVersaoBase64
      });

      const versoesAtualizadas = await listarVersoesDataset(datasetId);
      setVersoesPorDataset((prev) => ({ ...prev, [datasetId]: versoesAtualizadas }));
      setVersaoSelecionadaParaCriacao(null);
      setDescricaoNovaVersao('');
      setArquivoNovaVersaoBase64('');
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao criar nova versão.';
      setErrorMessage(message);
    } finally {
      setLoadingCriacaoVersao(false);
    }
  };

  const formatarData = (criadoEm: string) => {
    const data = new Date(criadoEm);
    return Number.isNaN(data.getTime()) ? criadoEm : formatter.format(data);
  };

  return (
    <div className="home-page">
      <div className="home-shell">
        <header className="home-topbar">
          <div className="home-brand">
            <p className="home-kicker">Dashboard</p>
            <h1>Datasets</h1>
          </div>

          <div className="home-topbar-actions">
            <button type="button" className="home-primary-button" onClick={() => navigate('/datasets/novo')}>
              Cadastrar dataset
            </button>

            <div className="home-profile-menu">
              <button
                type="button"
                className="home-profile-button"
                onClick={() => setMenuAberto((value) => !value)}
                aria-expanded={menuAberto}
                aria-haspopup="menu"
                title="Menu do perfil"
              >
                <span className="home-profile-avatar">👤</span>
                <span className="home-profile-label">
                  <strong>{user.email}</strong>
                  <small>{user.cpf}</small>
                </span>
              </button>

              {menuAberto && (
                <div className="home-profile-dropdown" role="menu" aria-label="Menu do perfil">
                  <button type="button" onClick={() => navigate('/perfil')}>
                    Meu perfil
                  </button>
                  <button type="button" onClick={handleLogout}>
                    Sair da conta
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>

        <main className="home-content">
          <section className="home-section">
            <div className="home-section-header">
              <div>
                <p className="home-kicker">Catálogo</p>
                <h2>Listagem de datasets</h2>
              </div>
              <p className="home-section-summary">
                {loading ? 'Carregando...' : `${datasets.length} dataset${datasets.length === 1 ? '' : 's'} disponíveis`}
              </p>
            </div>

            {errorMessage && <p className="home-error">{errorMessage}</p>}

            {!errorMessage && loading && <p className="home-empty-state">Buscando datasets...</p>}

            {!loading && !errorMessage && datasets.length === 0 && (
              <p className="home-empty-state">Nenhum dataset cadastrado ainda.</p>
            )}

            {!loading && !errorMessage && datasets.length > 0 && (
              <div className="home-dataset-grid">
                {datasets.map((dataset) => (
                  <article key={dataset.id} className="home-dataset-card">
                    <div className="home-dataset-card-header">
                      <h3>{dataset.nome}</h3>
                      <span className="home-dataset-id">#{dataset.id}</span>
                    </div>

                    <p className="home-dataset-description">{dataset.descricao}</p>

                    <dl className="home-dataset-meta">
                      <div>
                        <dt>Fontes</dt>
                        <dd>{dataset.fontes || '—'}</dd>
                      </div>
                      <div>
                        <dt>Criador</dt>
                        <dd>{dataset.criadorCpf}</dd>
                      </div>
                      <div>
                        <dt>Criado em</dt>
                        <dd>{formatarData(dataset.criadoEm)}</dd>
                      </div>
                    </dl>

                    <div className="home-dataset-actions">
                      <button
                        type="button"
                        className="home-dataset-action-button"
                        onClick={() => handleExpandirDataset(dataset.id)}
                      >
                        {datasetExpandidoId === dataset.id ? 'Ocultar versões' : 'Ver versões'}
                      </button>
                    </div>

                    {datasetExpandidoId === dataset.id && (
                      <div className="home-versions-dropdown">
                        {loadingVersoes[dataset.id] && <p className="home-version-status">Carregando versões...</p>}

                        {!loadingVersoes[dataset.id] && (versoesPorDataset[dataset.id]?.length ?? 0) === 0 && (
                          <p className="home-version-status">Este dataset ainda não possui versões.</p>
                        )}

                        {!loadingVersoes[dataset.id] && (versoesPorDataset[dataset.id]?.length ?? 0) > 0 && (
                          <div className="home-versions-list">
                            {versoesPorDataset[dataset.id].map((versao) => (
                              <div key={`${dataset.id}-${versao.numeroVersao}`} className="home-version-item">
                                <div className="home-version-item-header">
                                  <strong>Versão {versao.numeroVersao}</strong>
                                  <small>Criada em {formatarData(versao.criadoEm)}</small>
                                </div>
                                <p>{versao.descModificacoes || 'Sem descrição de modificações.'}</p>
                                <div className="home-version-actions">
                                  <button
                                    type="button"
                                    className="home-dataset-action-button"
                                    onClick={() => handleBaixarVersao(dataset, versao.numeroVersao)}
                                  >
                                    Baixar
                                  </button>
                                  <button
                                    type="button"
                                    className="home-dataset-action-button"
                                    onClick={() => handleVisualizarVersao(dataset.id, versao.numeroVersao)}
                                  >
                                    Visualizar
                                  </button>
                                  <button
                                    type="button"
                                    className="home-dataset-action-button"
                                    onClick={() => handleSelecionarCriacaoDeVersao(dataset.id, versao.numeroVersao)}
                                  >
                                    Criar nova versão
                                  </button>
                                </div>
                                {visualizacaoVersao &&
                                  visualizacaoVersao.datasetId === dataset.id &&
                                  visualizacaoVersao.numeroVersao === versao.numeroVersao && (
                                    <div className="home-version-visualizacao">
                                      <p><strong>Criador:</strong> {visualizacaoVersao.criadorCpf}</p>
                                      <p>
                                        <strong>Versão base:</strong>{' '}
                                        {visualizacaoVersao.versaoBaseNumero ?? 'Original'}
                                      </p>
                                      <p><strong>Descrição:</strong> {visualizacaoVersao.descModificacoes || '—'}</p>
                                    </div>
                                  )}
                              </div>
                            ))}
                          </div>
                        )}

                        {versaoSelecionadaParaCriacao && versaoSelecionadaParaCriacao.datasetId === dataset.id && (
                          <form
                            className="home-new-version-form"
                            onSubmit={(event) => {
                              event.preventDefault();
                              void handleCriarNovaVersao(dataset.id);
                            }}
                          >
                            <h4>
                              Nova versão baseada na versão {versaoSelecionadaParaCriacao.numeroVersao}
                            </h4>
                            <label htmlFor={`descricao-versao-${dataset.id}`}>Descrição das modificações</label>
                            <input
                              id={`descricao-versao-${dataset.id}`}
                              type="text"
                              value={descricaoNovaVersao}
                              onChange={(event) => setDescricaoNovaVersao(event.target.value)}
                              required
                            />
                            <label htmlFor={`arquivo-versao-${dataset.id}`}>Arquivo CSV da nova versão</label>
                            <input
                              id={`arquivo-versao-${dataset.id}`}
                              type="file"
                              accept=".csv,text/csv"
                              onChange={(event) => {
                                void handleArquivoNovaVersao(event);
                              }}
                              required
                            />
                            <button
                              type="submit"
                              className="home-dataset-action-button"
                              disabled={loadingCriacaoVersao}
                            >
                              {loadingCriacaoVersao ? 'Salvando versão...' : 'Salvar nova versão'}
                            </button>
                          </form>
                        )}
                      </div>
                    )}
                  </article>
                ))}
              </div>
            )}
          </section>
        </main>
      </div>
    </div>
  );
}

export default Home;
