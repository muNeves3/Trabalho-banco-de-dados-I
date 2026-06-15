import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { 
  buscarDatasetPorId, 
  listarVersoes, 
  detalharVersao, 
  listarFeaturesPorVersao, 
  getDownloadUrl,
  cadastrarFeature,
  Dataset, 
  VersaoDataset, 
  Feature 
} from '../../api/api';
import './DatasetDetalhe.css';

type UserSession = {
  cpf: string;
  email: string;
};

type DatasetDetalheProps = {
  user: UserSession;
};

function DatasetDetalhe({ user }: DatasetDetalheProps) {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const datasetId = Number(id);

  const [dataset, setDataset] = useState<Dataset | null>(null);
  const [versoes, setVersoes] = useState<VersaoDataset[]>([]);
  const [versaoSelecionada, setVersaoSelecionada] = useState<number | null>(null);
  const [features, setFeatures] = useState<Feature[]>([]);
  const [loading, setLoading] = useState(true);
  const [loadingFeatures, setLoadingFeatures] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const [mostrarFormFeature, setMostrarFormFeature] = useState(false);
  const [featureNome, setFeatureNome] = useState('');
  const [featureTipo, setFeatureTipo] = useState('');
  const [featureDescricao, setFeatureDescricao] = useState('');
  const [salvandoFeature, setSalvandoFeature] = useState(false);
  const [featureMsg, setFeatureMsg] = useState('');

  const formatter = useMemo(
    () => new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' }),
    []
  );

  const formatarData = (data: string) => {
    const d = new Date(data);
    return Number.isNaN(d.getTime()) ? data : formatter.format(d);
  };

  useEffect(() => {
    const carregar = async () => {
      setLoading(true);
      try {
        const [ds, vs] = await Promise.all([
          buscarDatasetPorId(datasetId),
          listarVersoes(datasetId)
        ]);
        setDataset(ds);
        setVersoes(vs);
      } catch (error) {
        const msg = error instanceof Error ? error.message : 'Erro ao carregar dataset.';
        setErrorMessage(msg);
      } finally {
        setLoading(false);
      }
    };
    carregar();
  }, [datasetId]);

  const handleVisualizar = async (numeroVersao: number) => {
    if (versaoSelecionada === numeroVersao) {
      setVersaoSelecionada(null);
      setFeatures([]);
      setMostrarFormFeature(false);
      setFeatureMsg('');
      return;
    }

    setVersaoSelecionada(numeroVersao);
    setLoadingFeatures(true);
    setMostrarFormFeature(false);
    setFeatureMsg('');
    try {
      await detalharVersao(datasetId, numeroVersao, user.cpf);
      const feats = await listarFeaturesPorVersao(datasetId, numeroVersao);
      setFeatures(feats);
    } catch (error) {
      setFeatures([]);
    } finally {
      setLoadingFeatures(false);
    }
  };

  const handleDownload = (numeroVersao: number) => {
    const url = getDownloadUrl(datasetId, numeroVersao, user.cpf);
    window.open(url, '_blank');
  };

  const handleSalvarFeature = async () => {
    if (!versaoSelecionada) return;
    setSalvandoFeature(true);
    setFeatureMsg('');
    try {
      await cadastrarFeature({
        nome: featureNome,
        tipo: featureTipo,
        descricao: featureDescricao,
        datasetId: datasetId,
        numeroVersao: versaoSelecionada
      });
      const feats = await listarFeaturesPorVersao(datasetId, versaoSelecionada);
      setFeatures(feats);
      setFeatureNome('');
      setFeatureTipo('');
      setFeatureDescricao('');
      setFeatureMsg('Feature cadastrada com sucesso!');
    } catch (error) {
      const msg = error instanceof Error ? error.message : 'Erro ao cadastrar feature.';
      setFeatureMsg(msg);
    } finally {
      setSalvandoFeature(false);
    }
  };

  if (loading) return <div className="detalhe-page"><p>Carregando...</p></div>;
  if (errorMessage) return <div className="detalhe-page"><p className="error">{errorMessage}</p></div>;
  if (!dataset) return null;

  return (
    <div className="detalhe-page">
      <div className="detalhe-card">
        <div className="detalhe-header">
          <h1>{dataset.nome}</h1>
          <button type="button" className="link-button" onClick={() => navigate('/home')}>
            Voltar
          </button>
        </div>

        <div className="detalhe-info">
          <p>{dataset.descricao}</p>
          <p><strong>Fontes:</strong> {dataset.fontes || '—'}</p>
          <p><strong>Criador:</strong> {dataset.criadorCpf}</p>
          <p><strong>Criado em:</strong> {formatarData(dataset.criadoEm)}</p>
        </div>

        <hr className="detalhe-divisor" />

        <div className="detalhe-versoes-header">
          <h2>Versões ({versoes.length})</h2>
          <button
            type="button"
            className="detalhe-btn-nova-versao"
            onClick={() => navigate(`/datasets/${datasetId}/nova-versao`)}
          >
            + Nova Versão
          </button>
        </div>

        {versoes.length === 0 && <p>Nenhuma versão cadastrada.</p>}

        {versoes.map((v) => (
          <div key={v.numeroVersao} className="detalhe-versao">
            <div className="detalhe-versao-header">
              <strong>Versão {v.numeroVersao}</strong>
              <div className="detalhe-versao-acoes">
                <button
                  type="button"
                  className="detalhe-btn-visualizar"
                  onClick={() => handleVisualizar(v.numeroVersao)}
                >
                  {versaoSelecionada === v.numeroVersao ? 'Fechar' : 'Visualizar'}
                </button>
                <button
                  type="button"
                  className="detalhe-btn-baixar"
                  onClick={() => handleDownload(v.numeroVersao)}
                >
                  Baixar
                </button>
              </div>
            </div>

            {versaoSelecionada === v.numeroVersao && (
              <div className="detalhe-versao-body">
                <p><strong>Criador:</strong> {v.criadorCpf}</p>
                <p><strong>Criado em:</strong> {formatarData(v.criadoEm)}</p>
                <p><strong>Base:</strong> {v.versaoBaseNumero ? `Versão ${v.versaoBaseNumero}` : 'Original'}</p>
                <p><strong>Modificações:</strong> {v.descModificacoes || '—'}</p>

                <div className="detalhe-features">
                  <div className="detalhe-features-header">
                    <h3>Features</h3>
                    <button
                      type="button"
                      className="detalhe-btn-add-feature"
                      onClick={() => setMostrarFormFeature(!mostrarFormFeature)}
                    >
                      {mostrarFormFeature ? 'Cancelar' : '+ Adicionar'}
                    </button>
                  </div>

                  {loadingFeatures && <p>Carregando features...</p>}
                  {!loadingFeatures && features.length === 0 && <p>Nenhuma feature cadastrada.</p>}
                  {!loadingFeatures && features.length > 0 && (
                    <ul>
                      {features.map((f) => (
                        <li key={f.id}>
                          <strong>{f.nome}</strong> ({f.tipo}) — {f.descricao}
                        </li>
                      ))}
                    </ul>
                  )}

                  {mostrarFormFeature && (
                    <div className="detalhe-form-feature">
                      <label>Nome</label>
                      <input
                        type="text"
                        value={featureNome}
                        onChange={(e) => setFeatureNome(e.target.value)}
                        required
                      />
                      <label>Tipo</label>
                      <input
                        type="text"
                        value={featureTipo}
                        onChange={(e) => setFeatureTipo(e.target.value)}
                        required
                      />
                      <label>Descrição</label>
                      <input
                        type="text"
                        value={featureDescricao}
                        onChange={(e) => setFeatureDescricao(e.target.value)}
                        required
                      />
                      <button
                        type="button"
                        className="detalhe-btn-salvar-feature"
                        onClick={handleSalvarFeature}
                        disabled={salvandoFeature || !featureNome || !featureTipo || !featureDescricao}
                      >
                        {salvandoFeature ? 'Salvando...' : 'Salvar Feature'}
                      </button>
                      {featureMsg && (
                        <p className={featureMsg.includes('sucesso') ? 'detalhe-sucesso' : 'error'}>
                          {featureMsg}
                        </p>
                      )}
                    </div>
                  )}
                </div>

                {/* <button
                  type="button"
                  className="detalhe-btn-baixar"
                  onClick={() => handleDownload(v.numeroVersao)}
                  style={{ marginTop: '12px' }}
                >
                  Baixar CSV
                </button> */}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}

export default DatasetDetalhe;