import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaSave } from 'react-icons/fa';

function RessourceForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [ressource, setRessource] = useState({
    nom: '',
    type: '',
    cout: '',
    disponibilite: false
  });
  const [error, setError] = useState(null);

  const typeOptions = ['Humaine', 'Matériel', 'Financière'];

  useEffect(() => {
    if (id) {
      fetch(`http://localhost:8080/api/ressources/${id}`)
        .then(res => res.json())
        .then(data => {
          setRessource({
            nom: data.nom || '',
            type: data.type || '',
            cout: data.cout || '',
            disponibilite: data.disponibilite || false
          });
        })
        .catch(err => setError(err.message));
    }
  }, [id]);

  const handleChange = (e) => {
    const { name, value, type: inputType, checked } = e.target;
    setRessource({
      ...ressource,
      [name]: inputType === 'checkbox' ? checked : value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError(null);

    if (!ressource.nom) {
      setError("Le nom de la ressource est requis.");
      return;
    }
    if (!ressource.type) {
      setError("Le type de la ressource est requis.");
      return;
    }
    if (!ressource.cout) {
      setError("Le coût de la ressource est requis.");
      return;
    }

    const method = id ? 'PUT' : 'POST';
    const url = id ? `http://localhost:8080/api/ressources/${id}` : 'http://localhost:8080/api/ressources';
    const payload = {
      nom: ressource.nom,
      type: ressource.type,
      cout: parseFloat(ressource.cout),
      disponibilite: ressource.disponibilite
    };

    console.log('Submitting payload:', payload);

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
      .then(res => {
        if (!res.ok) {
          return res.text().then(text => { throw new Error(text || 'Erreur lors de la sauvegarde'); });
        }
        return res.json();
      })
      .then(data => {
        console.log('Ressource saved:', data);
        navigate('/ressources');
      })
      .catch(err => {
        console.error('Submission error:', err);
        setError(err.message || 'Erreur inattendue lors de la sauvegarde');
      });
  };

  return (
    <div className="max-w-md mx-auto p-6 bg-white shadow-lg rounded-lg">
      <h2 className="text-3xl font-bold mb-6 text-gray-800">{id ? 'Modifier Ressource' : 'Ajouter Ressource'}</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block text-sm font-medium text-gray-700">Nom</label>
          <input
            type="text"
            name="nom"
            value={ressource.nom}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Type</label>
          <select
            name="type"
            value={ressource.type}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          >
            <option value="">Sélectionner un type</option>
            {typeOptions.map(option => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Coût</label>
          <input
            type="number"
            name="cout"
            value={ressource.cout}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">
            Disponibilité
            <input
              type="checkbox"
              name="disponibilite"
              checked={ressource.disponibilite}
              onChange={handleChange}
              className="ml-2"
            />
          </label>
        </div>
        <button
          type="submit"
          className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 flex items-center"
        >
          <FaSave className="mr-2" /> Sauvegarder
        </button>
      </form>
    </div>
  );
}

export default RessourceForm;