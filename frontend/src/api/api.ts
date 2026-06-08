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

  if (!response.ok) {
    throw new Error('CPF não encontrado.');
  }

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

  if (!response.ok) {
    throw new Error('Erro ao buscar usuários.');
  }

  return response.json();
}

export async function deletarUsuario(cpf: string): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/${cpf}`, {
    method: 'DELETE'
  });

  if (!response.ok) {
    await parseError(response, 'Erro ao deletar usuário.');
  }
}

export async function buscarPorId(id: number): Promise<Usuario> {
  const response = await fetch(`${API_BASE_URL}/api/usuarios/id/${id}`);

  if (!response.ok) {
    throw new Error('Usuário não encontrado.');
  }

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
  const response = await fetch(`${API_BASE_URL}/api/usuarios/id/${id}`, {
    method: 'DELETE'
  });

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

// tipos e funções para DATASETS

export type Dataset = {
  id: number;
  nome: string;
  descricao: string;
  fontes: string;
  criadorCpf: string;
  criadoEm: string;
};

export type DatasetPayload = {
  nome: string;
  descricao: string;
  fontes: string;
  criadorCpf: string;
};

export async function cadastrarDataset(payload: DatasetPayload): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/datasets`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    await parseError(response, 'Falha ao cadastrar dataset.');
  }
}

export async function listarDatasets(): Promise<Dataset[]> {
  const response = await fetch(`${API_BASE_URL}/api/datasets`);

  if (!response.ok) {
    throw new Error('Erro ao buscar datasets.');
  }

  return response.json();
}
