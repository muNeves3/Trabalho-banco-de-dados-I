import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { listarDatasets, Dataset } from '../../api/api';
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
                  <article 
                    key={dataset.id} 
                    className="home-dataset-card" 
                    onClick={() => navigate(`/datasets/${dataset.id}`)}
                    style={{ cursor: 'pointer' }}
                  >
                    <div className="home-dataset-card-header">
                      <h3>{dataset.nome}</h3>
                      <span className="home-dataset-id">#{dataset.id}</span>
                    </div>

                    <p className="home-dataset-description">{dataset.descricao}</p>

                    <dl className="home-dataset-meta">
                      <div>
                        <dt>Criador</dt>
                        <dd>{dataset.criadorCpf}</dd>
                      </div>
                      <div>
                        <dt>Criado em</dt>
                        <dd>{formatarData(dataset.criadoEm)}</dd>
                      </div>
                      <div>
                        <dt>Versões</dt>
                        <dd>{dataset.quantidadeVersoes}</dd>
                      </div>
                    </dl>
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
