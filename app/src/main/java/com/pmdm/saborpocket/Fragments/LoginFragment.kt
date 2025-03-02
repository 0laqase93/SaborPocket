package com.pmdm.saborpocket.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pmdm.saborpocket.Data.Aplicacion
import com.pmdm.saborpocket.Entities.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.pmdm.saborpocket.R
import com.pmdm.saborpocket.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var preferencias: SharedPreferences
    private lateinit var cambiadorVistas: ViewSwitcher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        cambiadorVistas = binding.viewSwitcher
        preferencias = requireActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)

        cargarLogicaInicioSesion()
        cargarLogicaRegistro()
        cargarAnimacionTransicion()

        return binding.root
    }

    private fun cargarLogicaInicioSesion() {
        val etEmail = binding.etEmail
        val etContrasena = binding.etPassword
        val cbRecordar = binding.cbRememberMe
        val btnIniciarSesion = binding.btnLogin
        val tvRegistro = binding.tvRegister

        val emailPref = preferencias.getString(getString(R.string.SharedEmail), null)
        val contrasenaPref = preferencias.getString(getString(R.string.SharedPassword), null)
        val recordarPref = preferencias.getBoolean(getString(R.string.SharedRemember), false)

        cbRecordar.isChecked = recordarPref

        if (emailPref != null && contrasenaPref != null) {
            etEmail.setText(emailPref)
            etContrasena.setText(contrasenaPref)
        }

        tvRegistro.setOnClickListener {
            cambiadorVistas.showNext()
        }

        btnIniciarSesion.setOnClickListener {
            val email = etEmail.text.toString()
            val contrasena = etContrasena.text.toString()

            lifecycleScope.launch {
                val usuarioLeido = leerUsuario(email)

                if (usuarioLeido != null && usuarioLeido.contrasena == contrasena) {
                    if (cbRecordar.isChecked) {
                        with(preferencias.edit()) {
                            putString(getString(R.string.SharedEmail), email)
                            putString(getString(R.string.SharedPassword), contrasena)
                            apply()
                        }
                    } else {
                        preferencias.edit().clear().apply()
                    }

                    Toast.makeText(requireContext(), "Bienvenido ${usuarioLeido.nombre}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                            Usuario = usuarioLeido
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cargarLogicaRegistro() {
        val btnRegistroIniciarSesion = binding.btnRegisterLogin

        btnRegistroIniciarSesion.setOnClickListener {
            val nombre = binding.etRegisterName.text.toString().trim()
            val email = binding.etRegisterEmail.text.toString().trim()
            val contrasena = binding.etRegisterPassword.text.toString()

            if (nombre.isNotEmpty() && email.isNotEmpty() && contrasena.isNotEmpty()) {
                lifecycleScope.launch {
                    val usuarioExistente = leerUsuario(email)

                    if (usuarioExistente != null) {
                        Toast.makeText(requireContext(), "Usuario duplicado", Toast.LENGTH_SHORT).show()
                    } else {
                        guardarUsuario(email, nombre, contrasena)
                        with(preferencias.edit()) {
                            putString(getString(R.string.SharedName), nombre)
                            putString(getString(R.string.SharedEmail), email)
                            putString(getString(R.string.SharedPassword), contrasena)
                            putBoolean(getString(R.string.SharedRemember), true)
                            apply()
                        }
                        cambiadorVistas.showPrevious()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Datos inválidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarAnimacionTransicion() {
        val animacionEntrada = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        val animacionSalida = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)
        cambiadorVistas.inAnimation = animacionEntrada
        cambiadorVistas.outAnimation = animacionSalida
    }

    private fun guardarUsuario(email: String, nombre: String, contrasena: String) {
        val nuevoUsuario = UsuarioEntity(email = email, nombre = nombre, contrasena = contrasena)
        lifecycleScope.launch(Dispatchers.IO) {
            Aplicacion.baseDeDatos.usuarioDao().agregarUsuario(nuevoUsuario)
        }
    }

    private suspend fun leerUsuario(email: String?): UsuarioEntity? {
        return email?.trim()?.let {
            withContext(Dispatchers.IO) {
                Aplicacion.baseDeDatos.usuarioDao().obtenerUsuario(it)
            }
        }
    }
}
