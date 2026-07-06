const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

type Usuario = {
  cpf: string;
  email: string;
  nome?: string;
};

type LoginPayload = {
  email: string;
  senhaHash: string;
};

type CadastroPayload = {
  cpf: string;
  email: string;
  senhaHash: string;
  nome: string;
};

async function parseError(response: Response, fallbackMessage: string) {
  const backendMessage = await response.text();
  throw new Error(backendMessage || fallbackMessage);
}

export async function buscarUsuarioPorCpf(cpf: string): Promise<Usuario> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/${cpf}`);
  if (!response.ok) throw new Error('CPF não encontrado.');
  return response.json();
}

export async function loginUsuario(payload: LoginPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    await parseError(response, 'Falha no login.');
  }
}

export async function cadastrarUsuario(payload: CadastroPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    await parseError(response, 'Falha no cadastro.');
  }
}

export async function listarUsuarios(): Promise<Usuario[]> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios`);
  if (!response.ok) throw new Error('Erro ao buscar usuários.');
  return response.json();
}

export async function deletarUsuario(cpf: string): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/${cpf}`, { method: 'DELETE' });
  if (!response.ok) {
    await parseError(response, 'Erro ao deletar usuário.');
  }
}

export async function buscarPorId(id: number): Promise<Usuario> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/id/${id}`);
  if (!response.ok) throw new Error('Usuário não encontrado.');
  return response.json();
}

export async function atualizarUsuario(cpf: string, payload: Partial<CadastroPayload>): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/${cpf}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    await parseError(response, 'Erro ao atualizar usuário.');
  }
}

export async function deletarUsuarioPorId(id: number): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/id/${id}`, { method: 'DELETE' });
  if (!response.ok) {
    await parseError(response, 'Erro ao deletar usuário.');
  }
}

export async function authenticateUsuario(payload: LoginPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/authenticate`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    await parseError(response, 'Falha ao autenticar.');
  }
}

// ==================== DATASETS ====================

export type Dataset = {
  id: number;
  nome: string;
  descricao: string;
  criadorCpf: string;
  criadoEm: string;
  quantidadeVersoes: number;
};

export type DatasetPayload = {
  nome: string;
  descricao: string;
  criadorCpf: string;
};

export async function cadastrarDataset(payload: DatasetPayload): Promise<{ id: number; mensagem: string }> {
  const response = await fetch(`${API_BASE_URL}/api/datasets`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    await parseError(response, 'Falha ao cadastrar dataset.');
  }
  return response.json();
}

export async function listarDatasets(): Promise<Dataset[]> {
  const response = await fetch(`${API_BASE_URL}/api/datasets`);
  if (!response.ok) throw new Error('Erro ao buscar datasets.');
  return response.json();
}

export async function buscarDatasetPorId(id: number): Promise<Dataset> {
  const response = await fetch(`${API_BASE_URL}/api/datasets/${id}`);
  if (!response.ok) throw new Error('Dataset não encontrado.');
  return response.json();
}

export async function listarFontes(datasetId: number): Promise<string[]> {
  const response = await fetch(`${API_BASE_URL}/api/fontes/${datasetId}`);
  if (!response.ok) throw new Error('Erro ao buscar fontes.');
  return response.json();
}

export async function adicionarFonte(datasetId: number, fonte: string): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/fontes/${datasetId}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(fonte)
  });
  if (!response.ok) {
    await parseError(response, 'Erro ao adicionar fonte.');
  }
}

// ==================== VERSÕES ====================

export type VersaoDataset = {
  datasetId: number;
  numeroVersao: number;
  versaoBaseNumero: number | null;
  criadorCpf: string;
  descModificacoes: string;
  criadoEm: string;
};

export async function listarVersoes(datasetId: number): Promise<VersaoDataset[]> {
  const response = await fetch(`${API_BASE_URL}/api/versoes/${datasetId}`);
  if (!response.ok) throw new Error('Erro ao buscar versões.');
  return response.json();
}

export async function detalharVersao(datasetId: number, numeroVersao: number, cpf: string): Promise<VersaoDataset> {
  const response = await fetch(`${API_BASE_URL}/api/versoes/${datasetId}/${numeroVersao}?cpf=${cpf}`);
  if (!response.ok) throw new Error('Erro ao buscar detalhes da versão.');
  return response.json();
}

export async function criarVersao(
  datasetId: number,
  numeroVersao: number,
  criadorCpf: string,
  descModificacoes: string,
  arquivo: File,
  versaoBaseNumero?: number
): Promise<void> {
  const formData = new FormData();
  formData.append('datasetId', datasetId.toString());
  formData.append('numeroVersao', numeroVersao.toString());
  formData.append('criadorCpf', criadorCpf);
  formData.append('descModificacoes', descModificacoes);
  formData.append('arquivo', arquivo);
  if (versaoBaseNumero !== undefined) {
    formData.append('versaoBaseNumero', versaoBaseNumero.toString());
  }

  const response = await fetch(`${API_BASE_URL}/api/versoes`, {
    method: 'POST',
    body: formData
  });
  if (!response.ok) {
    await parseError(response, 'Erro ao criar versão.');
  }
}

export function getDownloadUrl(datasetId: number, numeroVersao: number, cpf: string): string {
  return `${API_BASE_URL}/api/versoes/${datasetId}/${numeroVersao}/download?cpf=${cpf}`;
}

export async function uploadArquivo(datasetId: number, numeroVersao: number, arquivo: any) : Promise<void> {
  const formData = new FormData();
  formData.append('arquivo', arquivo);

  const response = await fetch(`${API_BASE_URL}/api/versoes/${datasetId}/${numeroVersao}/upload`, {
    method: 'POST',
    body: formData
  });
  if (!response.ok) {
    await parseError(response, 'Erro ao fazer upload do arquivo.');
  }
}

// ==================== FEATURES ====================

export type Feature = {
  id: number;
  nome: string;
  tipo: string;
  descricao: string;
  datasetId: number;
  numeroVersao: number;
};

export async function listarFeaturesPorVersao(datasetId: number, numeroVersao: number): Promise<Feature[]> {
  const response = await fetch(`${API_BASE_URL}/api/features/versao/${datasetId}/${numeroVersao}`);
  if (!response.ok) throw new Error('Erro ao buscar features.');
  return response.json();
}

export async function cadastrarFeature(feature: Omit<Feature, 'id'>): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/features`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(feature)
  });
  if (!response.ok) {
    await parseError(response, 'Erro ao cadastrar feature.');
  }
}

// ==================== ACESSO VERSÃO ====================

export type AcessoVersaoRequest = {
  usuarioCpf: string;
  datasetId: number;
  numeroVersao: number;
  tipoAcesso: string;
};

export type AcessoVersaoResponse = {
  usuarioCpf: string;
  datasetId: number;
  numeroVersao: number;
  tipoAcesso: string;
  acessadoEm: string;
};

export async function registrarAcesso(payload: AcessoVersaoRequest): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/acessos-versao`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  if (!response.ok) {
    await parseError(response, 'Falha ao registrar acesso.');
  }
}

export async function listarAcessos(): Promise<AcessoVersaoResponse[]> {
  const response = await fetch(`${API_BASE_URL}/api/acessos-versao`);
  if (!response.ok) throw new Error('Erro ao buscar todos os acessos.');
  return response.json();
}

export async function listarAcessosPorUsuario(cpf: string): Promise<AcessoVersaoResponse[]> {
  const response = await fetch(`${API_BASE_URL}/api/acessos-versao/usuario/${cpf}`);
  if (!response.ok) throw new Error('Erro ao buscar acessos do usuário.');
  return response.json();
}

export async function listarAcessosPorDataset(datasetId: number): Promise<AcessoVersaoResponse[]> {
  const response = await fetch(`${API_BASE_URL}/api/acessos-versao/dataset/${datasetId}`);
  if (!response.ok) throw new Error('Erro ao buscar acessos do dataset.');
  return response.json();
}


// ==================== RELATÓRIOS ====================

export type Relatorio4 = {
  total_downloads_mes: number;
  total_visualizacao_mes: number;
  mes: string;
}

export async function getRelatorio4(ano: number): Promise<Relatorio4[]> {
  const response = await fetch(`${API_BASE_URL}/api/graficos/relatorio4/${ano}`);
  if(!response.ok) throw new Error('Erro ao buscar relatório 4.');
  return response.json();
}

export type Relatorio2 = {
  datasetId: number;
  nomeDataset: string;
  totalDownloads: number;
  totalVisualizacoes: number;
  totalAcessos: number;
}

export async function getRelatorio2(): Promise<Relatorio2[]> {
  const response = await fetch(`${API_BASE_URL}/api/graficos/ranking`);
  if (!response.ok) throw new Error('Erro ao buscar relatório 2.');
  return response.json();
}

export type Relatorio5 = {
  datasetId: number;
  nomeDataset: string;
  totalVersoes: number;
  tempoMedio: string;
}

export type Relatorio1 = {
  totalDatasets: number;
  totalVersoes: number;
  usuariosCadastrados: number;
  mediaVersoesPorDataset: number;
}

export async function getRelatorio5(): Promise<Relatorio5[]> {
  const response = await fetch(`${API_BASE_URL}/api/graficos/versoes`);
  if (!response.ok) throw new Error('Erro ao buscar relatório 5.');
  return response.json();
}

export async function getRelatorio1(): Promise<Relatorio1> {
  const response = await fetch(`${API_BASE_URL}/api/graficos/relatorio1`);
  if (!response.ok) throw new Error('Erro ao buscar relatório 1.');
  return response.json();
}

export type Relatorio3Item = {
  nome: string;
  count: number;
}

export async function getRelatorio3Contribuintes(): Promise<Relatorio3Item[]> {
  const response = await fetch(`${API_BASE_URL}/api/graficos/usuarios/contribuintes`);
  if (!response.ok) throw new Error('Erro ao buscar usuários mais contribuintes.');
  return response.json();
}

export async function getRelatorio3Acessos(): Promise<Relatorio3Item[]> {
  const response = await fetch(`${API_BASE_URL}/api/graficos/usuarios/acessos`);
  if (!response.ok) throw new Error('Erro ao buscar usuários com mais acessos.');
  return response.json();
}

export async function getRelatorio3Downloads(): Promise<Relatorio3Item[]> {
  const response = await fetch(`${API_BASE_URL}/api/graficos/usuarios/downloads`);
  if (!response.ok) throw new Error('Erro ao buscar usuários com mais downloads.');
  return response.json();
}

export type Relatorio6 = {
  hora: number;
  totalVisualizacoes: number;
  totalDownloads: number;
  totalAcessos: number;
};

export async function getRelatorio6(): Promise<Relatorio6[]> {
  const response = await fetch(`${API_BASE_URL}/api/graficos/horarios`);
  if (!response.ok) throw new Error('Erro ao buscar relatório 6.');
  return response.json();
}