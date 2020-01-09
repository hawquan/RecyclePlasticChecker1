package com.example.recycleplasticchecker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_code_check_2.*

/**
 * A simple [Fragment] subclass.
 */
class CodeCheck_2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_check_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        peteBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_codeCheck_2_to_PETE_1)
        }

        hdpeBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_codeCheck_2_to_HDPE_2)
        }

        pvcBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_codeCheck_2_to_v_3)
        }

        ldpeBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_codeCheck_2_to_LDPE_4)
        }

        ppBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_codeCheck_2_to_PP_5)
        }

        psBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_codeCheck_2_to_PS_6)
        }

        otherBtn.setOnClickListener{
            view.findNavController().navigate(R.id.action_codeCheck_2_to_OTHER_7)
        }


    }


}
